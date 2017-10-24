<%@ page import="org.tsaap.assignments.interactions.ResponseSubmissionSpecification; org.tsaap.assignments.statement.ChoiceInteractionType" %>

<div class="ui hidden divider"></div>
<a class="ui ribbon label"><g:message code="statement.questionType.label"/></a>

<div id="section-questionType" class="required field" v-cloak>
  <div class="ui hidden divider"></div>

  <div class="ui stackable secondary menu">
    <div class="item">
      <div class="ui big button el-radio-button"
           v-bind:class="{ selected: questionType == 'CHOICE', secondary: questionType == 'CHOICE' }"
           v-on:click="selectQuestionType('CHOICE')">
        <g:message code="statement.questionType.choice.label"/>
      </div>
    </div>

    <div class="item">
      <div class="ui big button el-radio-button"
           v-bind:class="{ selected: questionType == 'OPEN', secondary: questionType == 'OPEN' }"
           v-on:click="selectQuestionType('OPEN')">
        <g:message code="statement.questionType.open.label"/>
      </div>
    </div>
  </div>

  <div ref="questionChoiceSetup"
       v-bind:style="{visibility: questionType === 'CHOICE' ? 'visible' : 'hidden'}">

    <div style="margin-bottom: 1em;">
      <span style="margin-right: 0.5em;"><g:message code="sequence.interaction.studentsSelect"/></span>

      <input type="hidden" name="choiceInteractionType" v-bind:value="choiceQuestionSpec.interactionType">

      <div ref="choiceInteractionType"
           class="ui inline dropdown">
        <div class="text"></div>
        <i class="dropdown icon"></i>
      </div>

      <span style="margin-right: 0.5em;">
        <g:message code="sequence.interaction.choicesIn"/>
      </span>

      <input type="hidden" id="itemCount" name="itemCount" v-bind:value="choiceQuestionSpec.nbItem">

      <div ref="choiceNbItem"
           class="ui inline dropdown">
        <div class="text"></div>
        <i class="dropdown icon"></i>
      </div>
      items.
    </div>

    <div class="field">
      <label>
        <span
            v-if="choiceQuestionSpec.interactionType === ChoiceInteractionTypeDefinition.EXCLUSIVE.id">
          <g:message code="statement.choiceSpecification.correctAnswer"/> :
        </span>
        <span v-else><g:message code="statement.choiceSpecification.correctAnswerList"/> :</span>
      </label>

      <div class="inline fields"
           v-show="choiceQuestionSpec.interactionType === ChoiceInteractionTypeDefinition.EXCLUSIVE.id">
        <div class="field" v-for="n in 10" v-show="n<=choiceQuestionSpec.nbItem">
          <div ref="choiceExclusiveAnswer" class="ui radio checkbox">
            <input type="radio"
                   tabindex="0"
                   name="exclusiveChoice"
                   class="hidden"
                   v-model="choiceQuestionSpec.exclusiveAnswer"
                   v-bind:value="n">
            <label>{{n}}</label>
          </div>
        </div>
      </div>

      <div class="inline fields"
           v-show="choiceQuestionSpec.interactionType === ChoiceInteractionTypeDefinition.MULTIPLE.id">
        <div class="field" v-for="n in 10" v-show="n<=choiceQuestionSpec.nbItem">
          <div ref="choiceExclusiveAnswer" class="ui checkbox">
            <input type="checkbox"
                   tabindex="0"
                   class="hidden"
                   name="expectedChoiceList"
                   v-model="choiceQuestionSpec.multipleAnswer"
                   v-bind:value="n">
            <label>{{n}}</label>
          </div>
        </div>
      </div>
    </div>
  </div>

  %{-- Standard fields to submit values with the form --}%
  <input type="hidden" id="hasChoices" name="hasChoices" v-bind:value="hasChoices"/>

</div>

<r:script>
  $(document)
      .ready(function () {

  });

  var QuestionTypeId = {
    OPEN: 'OPEN',
    CHOICE: 'CHOICE'
  };

  var ChoiceInteractionTypeDefinition = {
    EXCLUSIVE: {
      id: 'EXCLUSIVE',
      text: '${g.message(code: "statement.questionType.ExclusiveChoice.manner").replaceAll("'", "\\\\u0027")}'
    },
    MULTIPLE: {
      id: 'MULTIPLE',
      text: '${g.message(code: "statement.questionType.MultipleChoice.manner").replaceAll("'", "\\\\u0027")}'
    }
  };

  var ChoiceInteractionTypeList = [
    ChoiceInteractionTypeDefinition.EXCLUSIVE,
    ChoiceInteractionTypeDefinition.MULTIPLE
  ];

  <g:set var="expectedChoiceList"
         value="${sequenceInstance?.statement?.choiceSpecificationObject?.getExpectedChoiceList() ?: []}"/>
%{-- Init VueJS app --}%
  var app = new Vue({
    el: '#section-questionType',
    data: {
      QuestionTypeId: QuestionTypeId,
      ChoiceInteractionTypeDefinition: ChoiceInteractionTypeDefinition,
      ChoiceInteractionTypeList: ChoiceInteractionTypeList,
      questionType: ${(!sequenceInstance?.statement || sequenceInstance?.statement?.hasChoices()) ? 'QuestionTypeId.CHOICE' : 'QuestionTypeId.OPEN'},
      choiceQuestionSpec: {
        interactionType: ${(!sequenceInstance?.statement || sequenceInstance?.statement?.choiceSpecificationObject?.getChoiceInteractionType() == ChoiceInteractionType.EXCLUSIVE.name()) ? 'ChoiceInteractionTypeDefinition.EXCLUSIVE.id' : 'ChoiceInteractionTypeDefinition.MULTIPLE.id'} ,
        nbItem: ${sequenceInstance?.statement?.choiceSpecificationObject?.itemCount ?: 2},
        exclusiveAnswer: ${expectedChoiceList ? expectedChoiceList[0].index : 1},
        multipleAnswer: ${expectedChoiceList*.index ?: [1]}
  }

},
computed: {
   hasChoices: function() {
     return this.questionType === QuestionTypeId.CHOICE;
   }
},
watch: {
  'choiceQuestionSpec.nbItem': function(val) {
    if(this.choiceQuestionSpec.exclusiveAnswer > val) {
      this.choiceQuestionSpec.exclusiveAnswer = val;
    }

this.choiceQuestionSpec.multipleAnswer = _.reject(
   this.choiceQuestionSpec.multipleAnswer,
   function(a) {
      return a > val;
   }
);
}
},
methods: {
selectQuestionType: function(type) {
this.questionType = type;
}
},
mounted: function(){
var that = this;


$(that.$refs.choiceInteractionType).dropdown({
  values: _.collect(
    that.ChoiceInteractionTypeList,
    function(choiceInteractionType) {
       var item = {
         name: choiceInteractionType.text,
         value: choiceInteractionType.id
       };

       if(choiceInteractionType.id === that.choiceQuestionSpec.interactionType) {
         item.selected = true;
       }

       return item;
    }
  ),
  onChange: function(value) {
    Vue.nextTick(function() {
      that.choiceQuestionSpec.interactionType = value;
    });
  }

});

$(that.$refs.choiceNbItem).dropdown({
  values: _.times(9, function(i) {
    var item = {
      name: i+2,
      value: i+2
    };

    if(i+2 === that.choiceQuestionSpec.nbItem) {
      item.selected = true;
    }

    return item;
  }),
  onChange: function(value) {
    Vue.nextTick(function() {
      that.choiceQuestionSpec.nbItem = value;
    });
  }
});

$(that.$refs.choiceExclusiveAnswer).checkbox();

}
});
</r:script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css"
      type="text/css"/>

<r:style>
  .el-radio-button {
    position: relative !important;
    padding: 3em !important;
    margin-bottom: 1em !important;
  }

  .el-radio-button::after {
    color: lightgrey;
    font-family: FontAwesome;
    border: none;
    content: " ";
    font-size: 15px;
    position: absolute;
    top: -15px;
    left: 25%;
    transform: translateX(-50%);
    height: 30px;
    width: 30px;
    line-height: 30px;
    text-align: center;
    border-radius: 50%;
    background: white;
    border: 2px solid #ddd;
  }

  .el-radio-button.selected::after {
    color: hsla(215, 5%, 25%, 1);
    border: 2px solid #5484a5;
    content: "\f00c";
    background: white;
    box-shadow: 0px 2px 5px -2px hsla(0, 0%, 0%, 0.25);
  }

</r:style>