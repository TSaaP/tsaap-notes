package org.tsaap.notes

import grails.gorm.PagedResultList
import org.gcontracts.annotations.Requires
import org.gcontracts.annotations.Ensures
import org.tsaap.CustomPagedResultList
import org.tsaap.directory.User

class ContextService {

    /**
     * Add a new context
     * @param context
     * @return
     */

    public static final String SUFFIXE_COPY = "-copy"

    NoteService noteService

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
        Context.findAllByOwner(user, paginationAndSorting)
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
        saveContext(newContext,true)
        if (duplicateQuestions) {
            List<Note> notes = noteService.findAllNotesAsQuestionForContext(aContext)
            notes.each { Note note ->
                noteService.duplicateNoteInContext(note, newContext, aUser)
            }
        }
        newContext
    }


}
