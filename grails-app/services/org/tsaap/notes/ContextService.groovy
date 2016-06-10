/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.notes

import groovy.sql.Sql
import org.gcontracts.annotations.Ensures
import org.gcontracts.annotations.Requires
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.tsaap.CustomPagedResultList
import org.tsaap.directory.User
import org.tsaap.lti.LmsContextHelper
import org.tsaap.lti.LmsContextService
import org.tsaap.questions.LiveSessionService

import javax.sql.DataSource

class ContextService {


    public static final String SUFFIXE_COPY = "-copy"

    NoteService noteService
    SessionFactory sessionFactory
    DataSource dataSource
    LmsContextService lmsContextService
    LmsContextHelper lmsContextHelper
    Sql sql
    LiveSessionService liveSessionService

    /**
     * Add a new context
     * @param context
     * @return
     */
    @Requires({ context })
    Context saveContext(Context context, Boolean flush = false) {
        context.save(flush: flush)
        context
    }

    @Requires({ user && context?.owner == user })
    Context updateContext(Context context, User user, Boolean flush = false) {
        saveContext(context, flush)
    }

    /**
     * Find all contexts for an owner
     * @param user the owner
     * @param paginationAndSorting the params specifying the pagination an sorting
     * @return
     */
    @Requires({ user })
    List<Context> contextsForOwner(User user,
                                   def paginationAndSorting = [sort: 'contextName']) {
        Context.findAllByOwnerAndRemoved(user,false, paginationAndSorting)
    }

    /**
     * Find all contexts followed by a user
     * @param user the follower
     * @param paginationAndSorting pagination an sorting params
     * @return
     */
    @Requires({ user })
    List<ContextFollower> contextFollowersByUser(User user,
                                                 def paginationAndSorting = [max: 10, offset: 0, sort: 'contextName', order: 'asc']) {
        def criteria = ContextFollower.createCriteria()
        def results = criteria.list(max: paginationAndSorting.max, offset: paginationAndSorting.offset) {
            eq 'follower', user
            eq 'isNoMoreSubscribed', false
            context {
                order paginationAndSorting.sort, paginationAndSorting.order
                if (paginationAndSorting.max) {
                    maxResults paginationAndSorting.max
                }
                if (paginationAndSorting.offset) {
                    offset paginationAndSorting.offset
                }
                eq('removed',false)
            }
            join 'context'
        }
        results
    }

    /**
     * Find all contexts followed by a user
     * @param user the follower
     * @param paginationAndSorting pagination an sorting params
     * @return
     */
    @Requires({ user })
    List<Context> contextsFollowedByUser(User user,
                                         def paginationAndSorting = [max: 10, offset: 0, sort: 'contextName', order: 'asc']) {
        CustomPagedResultList res = new CustomPagedResultList()
        def contextFollowers = contextFollowersByUser(user, paginationAndSorting)
        res.addAll(contextFollowers*.context)
        res.totalCount = contextFollowers.totalCount
        res
    }

    /**
     * Delete a given context
     * @param context the context to delete
     */
    @Requires({
        context && context.owner == user && !context.hasNotes()
    })
    def deleteContext(Context context, User user, Boolean flush = false) {
        def contextFollowers = ContextFollower.where { context == context }
        contextFollowers.deleteAll()
        sql = new Sql(dataSource)
        lmsContextHelper = new LmsContextHelper()
        lmsContextService.lmsContextHelper = lmsContextHelper
        lmsContextService.deleteLmsContextForContext(sql, context.id)
        context.delete(flush: flush)
    }

    /**
     * Subscribe as follower a user on a context
     * @param user the user to subscribe
     * @param context the context
     * @param followerIsTeacher the flag to indicate if the follower is a teacher
     * on the context
     * @return the context follower object if exists
     */
    @Requires({ context && user && context.owner != user })
    @Ensures({ !contextFollower.isNoMoreSubscribed })
    ContextFollower subscribeUserOnContext(User user, Context context,
                                           Boolean followerIsTeacher = false) {
        ContextFollower contextFollower = ContextFollower.findByFollowerAndContext(user, context)
        if (contextFollower) {
            contextFollower.unsusbscriptionDate = null
            contextFollower.isNoMoreSubscribed = false
            contextFollower.followerIsTeacher = followerIsTeacher
        } else {
            contextFollower = new ContextFollower(follower: user,
                    context: context,
                    followerIsTeacher: followerIsTeacher)
        }
        contextFollower.save()
        contextFollower
    }

    /**
     * Unsubscribe a user from a context ; the context follower object is not deleted.
     * It is marked as unsubscription
     * @param user the user to unsubscribe
     * @param context the context
     * @return the Context follower objet updated
     */
    @Requires({ context && user })
    @Ensures({ contextFollower?.isNoMoreSubscribed })
    ContextFollower unsuscribeUserOnContext(User user, Context context) {
        ContextFollower contextFollower = ContextFollower.findByFollowerAndContext(user, context)
        if (contextFollower) {
            contextFollower.unsusbscriptionDate = new Date()
            contextFollower.isNoMoreSubscribed = true
            contextFollower.save()
        }
        contextFollower
    }

    /**
     * Duplicate a context
     * @param aContext the context to duplicate
     * @param aUser the user duplicating the context
     * @param duplicateQuestions flag indicating if the questions have to be dublicated (default = true)
     * @return the new context
     */
    @Requires({ aUser && aContext?.owner == aUser })
    Context duplicateContext(Context aContext, User aUser, boolean duplicateQuestions = true) {
        Context newContext = new Context(
                contextName: "${aContext.contextName}$SUFFIXE_COPY",
                owner: aUser,
                url: aContext.url,
                descriptionAsNote: aContext.descriptionAsNote
        )
        saveContext(newContext, true)
        if (duplicateQuestions) {
            List<Note> notes = noteService.findAllNotesAsQuestionForContext(aContext)
            notes.each { Note note ->
                if (note.author == aUser) {
                    noteService.duplicateNoteInContext(note, newContext, aUser)
                }
            }
        }
        newContext
    }

    /**
     * Find all n phases session id for a context
     * @param context the context
     * @return the list of live session id
     */
    List findAllNphaseLiveSessionIdsForContext(Context context) {
        String query = 'select `live_session`.id from `live_session`, `session_phase`,`note`, `context` where `session_phase`.`live_session_id` = `live_session`.`id` and `live_session`.`note_id` = `note`.id and `note`.`context_id` = `context`.id and `session_phase`.`rank` = 3 and `session_phase`.`status` = \'Ended\' and `context`.id = :contextId'
        Session currentSession = sessionFactory.currentSession
        SQLQuery sqlQuery = currentSession.createSQLQuery(query)
        def queryResults = sqlQuery.with {
            setLong('contextId', context.id)
            list()
        }
        queryResults

    }
    /**
     * Close a context
     * @param context the context
     * @return the new context
     */
    @Requires({ user && context?.owner == user })
    Context openScope(Context context, User user) {
        context.closed = false
        context.save()
        context
    }
    /**
     * Open a context
     * @param context the context
     * @param user the user closing the context
     * @return the new context
     */
    @Requires({ user && context?.owner == user })
    Context closeScope(Context context, User user) {
        liveSessionService.closeAllLiveSessionForContext(context)
        context.closed = true
        context.save()
        context
    }
}
