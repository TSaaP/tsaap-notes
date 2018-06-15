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
<r:require module="elaastic_graph_result"></r:require>

<div style="text-align: center;">
    <div id='vega-view'></div>
</div>


<div style='font-size: 1rem;' id='interaction_${interactionInstance.id}_result'>
    <g:set var='choiceSpecification' value='${interactionInstance.sequence.statement.getChoiceSpecificationObject()}'/>
    <g:if test='${interactionInstance.sequence.statement.hasChoices()}'>

        <r:script>
(function() {
var i18n = {
  percentageOfVoters: '${g.message(code: "player.sequence.result.percentageOfVoters").replaceAll("'", "\\\\u0027")}',
  choice: '${g.message(code: "player.sequence.interaction.choice.label").replaceAll("'", "\\\\u0027")}'
};

elaastic.renderGraph(
  '#vega-view',
  ${raw(interactionInstance.sequence.statement.choiceSpecification)},
  ${raw(interactionInstance.results)},
   [],
   i18n   
);
}());
        </r:script>

        <g:set var='choiceSpecification'
               value='${interactionInstance.sequence.statement.getChoiceSpecificationObject()}'/>

        <g:set var='resultsByAttempt' value='${interactionInstance.resultsByAttempt()}'/>
        <g:set var='resultList' value='${resultsByAttempt['1']}'/>
        <g:set var='resultList2' value='${resultsByAttempt['2']}'/>
        <g:each var='i' in='${(1..choiceSpecification.itemCount)}'>
            <g:set var='choiceStatus'
                   value='${choiceSpecification.expectedChoiceListContainsChoiceWithIndex(i) ? 'green' : 'red'}'/>
            <g:set var='percentResult' value='${resultList?.get(i)}'/>
            <g:set var='percentResult2' value='${resultList2?.get(i)}'/>
        </g:each>
        <g:set var='percentResult' value='${resultList?.get(0)}'/>
        <g:set var='percentResult2' value='${resultList2?.get(0)}'/>

    </g:if>
</div>




