package org.tsaap.notes

import org.tsaap.directory.User

class ContextService {

  /**
   * Add a new context
   * @param context
   * @return
   */
  Context saveContext(Context context, Boolean flush = false) {
    if (context == null) {
      throw new IllegalArgumentException("context_not_found")
    }
    context.save(flush: flush)
    context
  }

  /**
   * Find all contexts for an owner
   * @param user the owner
   * @param paginationAndSorting the params specifying the pagination an sorting
   * @return
   */
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
  List<ContextFollower> contextsFollowedByUser(User user,
                                               def paginationAndSorting = [sort: 'contextName', order: 'asc']) {
    def criteria = ContextFollower.createCriteria()
    def results = criteria {
      follower = user
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
   * Delete a given context
   * @param context the context to delete
   */
  def deleteContext(Context context, Boolean flush = false) {
    if (context == null) {
      throw new IllegalArgumentException("context_not_found")
    }
    if (!context.hasNotes()) {
      context.delete(flush: flush)
    } else {
      throw new Exception("notes_exist_for_this_context")
    }
  }

}
