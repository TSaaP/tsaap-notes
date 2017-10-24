<%@ page import="grails.converters.JSON" %>
<div class="ui hidden divider"></div>

<div id="explanation-section">
  <a id="label-explanations"
     v-on:click="toggleSection"
     class="ui ribbon label">
    <g:message code="sequence.explanation.label"/> <i
      v-bind:class="{'caret left icon': !isSectionOpen, 'caret down icon': isSectionOpen}"></i>
  </a>

  <div xmlns:v-on="http://www.w3.org/1999/xhtml"
       xmlns:v-bind="http://www.w3.org/1999/xhtml"
       class="ui accordion"
       id="explanation-section-content" style="margin-top: 1em;">

    <div class="title" style="display: none;">
      <i class="dropdown icon"></i>
      Invisible title...
    </div>

    <div class="content">
      <h5 class="ui top attached block header">
        <g:message code="statement.expectedExplanation.label"/>
      </h5>

      <div class="ui bottom attached segment" id="expectedExplanationBloc">
        <textarea ref="initialExpectedExplanation" style="display: none;">
          ${statementInstance?.expectedExplanation}
        </textarea>
        <vue-ckeditor v-bind:id="'expectedExplanation'"
                      v-bind:name="'expectedExplanation'"
                      placeholder="${g.message(code: "statement.expectedExplanation.type.here")}"
                      v-model="expectedExplanation"
                      v-bind:config="ckeditorConfig"/>

      </div>

      <div>

        <h5 class="ui top attached block header">
          <g:message code="statement.fakeExplanations.label" />
        </h5>

        <div class="ui attached segment" v-for="index in fakeExplanationList.length"
             v-if="fakeExplationIsNotNull(index-1)">
          <template v-if="isOpenEnded">
            <button class="ui tiny negative icon right floated  button"
                    style="margin-left: 3em;"
                    type="button"
                    v-on:click="removeFakeExplanation(index-1)">
              <i class="trash icon"></i>
            </button>

            <div class="ui top left attached label">
              {{ index }}
            </div>

            <div class="ui hidden divider"></div>

          </template>
          <template v-else>

            <div class="ui inline field">
              <label><g:message code="statement.fakeExplanation.correspondingItem"/></label>
              <select v-model="fakeExplanationList[index-1].correspondingItem"
                      v-bind:name="'fakeExplanations['+(index-1)+'].correspondingItem'">
                <option v-for="choiceIndex in itemCount" v-bind:value="choiceIndex">
                  {{ choiceIndex }}
                </option>
              </select>
              <button class="ui tiny negative icon button"
                      style="margin-left: 3em;"
                      type="button"
                      v-on:click="removeFakeExplanation(index-1)">
                <i class="trash icon"></i>
              </button>
            </div>
          </template>


          <vue-ckeditor v-bind:name="'fakeExplanations['+(index-1)+'].content'"
                        v-model="fakeExplanationList[index-1].content"
                        v-bind:config="ckeditorConfig"
                        placeholder="${g.message(code: "statement.addFakeExplanation.type.here")}"
                        v-on:shouldPreventInfiniteLoop="shouldPreventInfiniteLoop()"></vue-ckeditor>

        </div>

        %{--<div class="ui hidden divider"></div>--}%

        <button class="ui secondary bottom attached fluid button"
                id="addFakeExplanationButton" type="button" v-on:click="addFakeExplanation()">
          <i class="plus icon"></i>
          <g:message code="statement.addFakeExplanation.label"/>
        </button>
      </div>
    </div>
  </div>
</div>

<r:script>
      initExplanations();

    function initExplanations() {
      CKEDITOR.disableAutoInline = true;
    }

    new Vue({
        el: '#explanation-section',
        data: {
            isSectionOpen: false,
            expectedExplanation: null,
            fakeExplanationList: [],
            statementId: "${statementInstance?.id}",
            itemCount: parseInt($('#itemCount').val()),
            isOpenEnded: $('#hasChoices').val() === 'false',
            ckeditorConfig: {
                customConfig: '/tsaap-notes/ckeditor/config.js',
                height: 100
            }
        },
        mounted: function() {
          var that = this;

          that.expectedExplanation = $(that.$refs.initialExpectedExplanation).val();

  that.accordion = $('#explanation-section-content').accordion({
      onOpen: function() {
        Vue.nextTick(function() {
          that.isSectionOpen = true;
        });
      },
      onClose: function() {
        Vue.nextTick(function() {
          that.isSectionOpen = false;
        });
      }
  });

  <g:if
      test="${statementInstance?.id && (statementInstance?.expectedExplanation || statementInstance?.fakeExplanations)}">
    $('#explanation-section-content').accordion('open', 0);
    this.isSectionOpen = true;
  </g:if>
  },
  created: function() {

      if (this.statementId) {
          this.$http.get('/tsaap-notes/sequence/findAllFakeExplanation/'+this.statementId).then(function(response)  {
              this.fakeExplanationList = response.body;
          }, function(response) {
              alert('An error occured contacting the server: '+response.body);
          });
      }
  },

  beforeUpdate: function() {
    this.isOpenEnded = $('#hasChoices').val() === 'false';
    this.itemCount = parseInt($('#itemCount').val());
  },

  methods: {
      toggleSection: function() {
        this.isSectionOpen = !this.isSectionOpen;
        if(this.isSectionOpen) {
          this.accordion.accordion('open', 0)
        }
        else {
          this.accordion.accordion('close', 0)
        }

      },

      addFakeExplanation: function() {
          this.fakeExplanationList.push({content: '', correspondingItem: 1});
      },

      removeFakeExplanation: function(index) {
          this.fakeExplanationList[index].content = null;
      },

      fakeExplationIsNotNull: function(index) {
          return this.fakeExplanationList[index].content !== null
      }

  }
});
</r:script>