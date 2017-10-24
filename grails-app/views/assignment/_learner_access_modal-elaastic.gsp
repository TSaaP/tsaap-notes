%{--
-
-  Copyright (C) 2017 Ticetime
-
-      This program is free software: you can redistribute it and/or modify
-      it under the terms of the GNU Affero General Public License as published by
-      the Free Software Foundation, either version 3 of the License, or
-      (at your option) any later version.
-
-      This program is distributed in the hope that it will be useful,
-      but WITHOUT ANY WARRANTY; without even the implied warranty of
-      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-      GNU Affero General Public License for more details.
-
-      You should have received a copy of the GNU Affero General Public License
-      along with this program.  If not, see <http://www.gnu.org/licenses/>.
-
--}%


<div class="ui small modal" id="learnerAccessModalApp">
  <i class="close icon"></i>

  <div class="header">

    <g:message code="assignment.learnerAccess.oneLine"/>
  </div>

  <div class="content">
    <div class="description">
      <p v-html="translatedCurrentAssignmentLearnerUrlNotice"></p>

      <div class="ui action fluid input">
        <input id="studentUrl"
               type="text"
               v-model="currentAssignmentLearnerUrl" readonly>
        <button class="ui right icon button" onclick="document.execCommand('copy');">
          <i class="copy icon"></i>
        </button>
      </div>
    </div>
  </div>

  <div class="actions">
    <div class="ui cancel button">
      OK
    </div>
  </div>
</div>

<r:script>
$(document)
      .ready(function () {
        %{-- Init VueJS app --}%
  window.learnerAccessModalApp = new Vue({
    el: '#learnerAccessModalApp',
    data: {
      currentAssignmentTitle: null,
      currentAssignmentLearnerUrl: null
    },
    computed: {
      translatedCurrentAssignmentLearnerUrlNotice: function() {
        return '${raw(g.message(code: "assignment.learnerAccess.description").replaceAll("'", "\\\\u0027"))}'.replace('#title', this.currentAssignmentTitle);
      }
    },
    methods: {
      showLearnerAccessModal: function(url, title) {
        this.currentAssignmentLearnerUrl = url;
        this.currentAssignmentTitle = title;

        $('#learnerAccessModalApp').modal(
          {
             onVisible: function() {
               $('#studentUrl').focus().select();
             }
          }
        ).modal('show');
      }

    }
  });
  });
</r:script>