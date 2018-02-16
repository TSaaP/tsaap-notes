/*
 *
 *  Copyright (C) 2017 Ticetime
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Affero General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Affero General Public License for more details.
 *
 *      You should have received a copy of the GNU Affero General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * @author John Tranier
 */
var elaastic = elaastic || {};

elaastic.util = {
  /**
   * Test if an element is visible in a scrollable container
   * Note : by visible, we mean the target element is in the displayed part of the container
   * with regards to the scroll (we do not consider if the element is hidden by CSS)
   * @param target the element to test
   * @param container the scrollable container
   * @param targetMinimumVisibleHeight the minimum height we want to see of the target element
   * @returns {boolean} true if the target is visible, false otherwise
   */
  isVisible: function (target, container, targetMinimumVisibleHeight) {
    var targetOffsetTop = target.offset().top;
    var containerScrollTop = container.scrollTop();
    var containerHeight = container.height();

    return (targetOffsetTop > containerScrollTop) &&
      (targetOffsetTop + targetMinimumVisibleHeight < containerScrollTop + containerHeight);
  },
  ensureIsVisible: function (target, container) {
    container.scrollTop(target.offset().top - (container.height() / 2));
  }
};
