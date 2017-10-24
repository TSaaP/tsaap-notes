<div id="appTitle" v-cloak>
  <a class="ui ribbon label"><g:message code="sequence.statement.title.label" default="Title"/></a>

  <div class="field ${hasErrors(bean: statementInstance, field: 'title', 'error')}"
       style="margin-top: 1em;"
       v-on:mouseover="mouseOverTitle"
       v-on:mouseleave="mouseLeaveTitle">
    <div class="ui required input" style="position: relative; overflow: hidden">
      <g:textField name="title"
                   id="title"
                   value="${raw(statementInstance?.title)}"
                   v-bind:class="{onlyShow: !modeEdit && !shouldShowNotice}"
                   placeholder="${g.message(code: "sequence.statement.title.type.here")}"
                   v-on:focus="startEditing"
                   v-on:blur="stopEditing"
                   size="75"/>

      <div class="ui right corner label" v-show="shouldShowNotice" style="line-height: unset">
        <i class="edit icon"></i>
      </div>

    </div>
  </div>

  <div class="ui hidden divider"></div>


  <div v-show="hasAttachment">
    <a class="ui ribbon label" style="margin-bottom: 1em;">Pièce jointe</a>

    <g:set var="attachment" value="${statementInstance?.attachment}"/>
    <g:if test="${attachment != null}">
      <div>
        <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>

        <g:remoteLink controller="sequence"
                      class="ui tiny negative right floated button"
                      style="margin-left: 2em;"
                      action="removeAttachement"
                      id="${statementInstance.id}"
                      onComplete="window.handleAttachmentDeleted()">
          <i class="trash icon"></i> Supprimer
        </g:remoteLink>
      </div>
    </g:if>
    <g:else>
      <div id="attachment">
        <input type="file"
               name="myFile"
               id="myFile" v-on:change="handleAttachmentSelected"/>
      </div>
    </g:else>
    <div class="ui hidden clearing divider"></div>
  </div>

</div>

<r:style>
  #title {
      font-weight: bold;
      font-size: 1.125rem;
  }

  #title.onlyShow {
    border-color: white; !important
  }
</r:style>

<r:script>
  new Vue({
    el: '#appTitle',
    data: {
      modeEdit: false,
      shouldShowNotice: false,
      hasAttachment: ${statementInstance?.attachment ? 'true' : 'false'}
  },
  mounted: function() {
      var that = this;
    window.handleAttachmentDeleted = function() {
      Vue.nextTick(function() {
        that.hasAttachment = false;
      });
    }
  },
  methods: {
    startEditing: function () {
      this.modeEdit = true;
      this.shouldShowNotice = false;
    },
    stopEditing: function () {
      this.modeEdit = false;
      this.shouldShowNotice = false;
    },
    mouseOverTitle: function () {
      this.shouldShowNotice = !this.modeEdit;
    },
    mouseLeaveTitle: function () {
      this.shouldShowNotice = false;
    },
    handleAttachmentSelected: function(e) {
      this.hasAttachment = true;
    },
    handleAttachmentDeleted: function() {
      this.hasAttachment = false;
    }
  }
});
</r:script>

<div id="statement-section">
  <a class="ui ribbon label">Énoncé</a>

  <r:style>
    #statement-container.basic.segment {
      border: 1px solid white;
    }
  </r:style>
  <div id="statement-container" class="ui large text segment"
       v-bind:class="{basic: !shouldShowNotice}"
       v-on:mouseover="mouseOverStatement"
       v-on:mouseleave="mouseLeaveStatement"
       style="overflow: hidden">
    <a class="ui right corner label" v-show="shouldShowNotice">
      <i class="edit icon"></i>
    </a>

    <textarea ref="initialContent" style="display: none;">
      ${sequenceInstance?.content}
    </textarea>

    <vue-ckeditor v-bind:id="'content'"
                  v-bind:name="'content'"
                  v-model="content"
                  placeholder="${g.message(code: "sequence.statement.content.type.here")}"
                  v-on:focus="startEditing"
                  v-on:blur="stopEditing"
                  v-bind:config="ckeditorConfig"/>
  </div>

  <r:script>
    CKEDITOR.disableAutoInline = true;

    new Vue({
      el: '#statement-section',
      data: {
        content: '',
        modeEdit: false,
        shouldShowNotice: false,
        ckeditorConfig: {
          customConfig: '/tsaap-notes/ckeditor/config.js'
        }
      },
      mounted: function () {
        var that = this;
        that.content = $(that.$refs.initialContent).val();
      },
      methods: {
        startEditing: function () {
          this.modeEdit = true;
          this.shouldShowNotice = false;
        },
        stopEditing: function () {
          this.modeEdit = false;
          this.shouldShowNotice = false;
        },
        mouseOverStatement: function () {
          this.shouldShowNotice = !this.modeEdit;
        },
        mouseLeaveStatement: function () {
          this.shouldShowNotice = false;
        }
      }
    });
  </r:script>
</div>