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


<r:script>

  var faceToFaceNotice = $('#context-faceToFace-notice');
  var distanceNotice = $('#context-distance-notice');
  var blendedNotice = $('#context-blended-notice');


  function manageConfigurationChange(sequenceId, questionType, sourceEvent) {
    var phaseConfrontationPanel = $('#phaseConfrontation_' + sequenceId);
    var phaseFirstSubmission = $('#phaseFirstSubmission_' + sequenceId);
    if (questionType == "OpenEnded") {
      sourceEvent.prop("checked", true);
      phaseFirstSubmission.hide();
      sourceEvent.val(true);
      phaseConfrontationPanel.show();
    } else if (sourceEvent.is(':checked')) {
      sourceEvent.val(true);
      phaseConfrontationPanel.show();
    } else {
      sourceEvent.val(false);
      phaseConfrontationPanel.hide();
    }
  }

  function manageExecutionContext(sequenceId, questionType, sourceEvent) {
    var studentsProvideExplanation = $('#studentsProvideExplanation_' + sequenceId + '_' + questionType);
    var configurationPanel = $('#configuration_' + sequenceId);


    switch (sourceEvent.val()) {
    case 'FaceToFace':
      distanceNotice.hide();
      blendedNotice.hide();
      faceToFaceNotice.show();
      break;

    case 'Distance':
      faceToFaceNotice.hide();
      blendedNotice.hide();
      distanceNotice.show();
      break;

    case 'Blended':
      faceToFaceNotice.hide();
      distanceNotice.hide();
      blendedNotice.show();
      break;

    default:
      throw('Unknown execution context');
    }

    switch (sourceEvent.val()) {
    case 'FaceToFace':
      configurationPanel.show();
      studentsProvideExplanation.prop("disabled", false);
      manageConfigurationChange(sequenceId, questionType, studentsProvideExplanation);
      break;
    default:
      studentsProvideExplanation.prop("checked", true);
      studentsProvideExplanation.prop("disabled", true);
      manageConfigurationChange(sequenceId, questionType, studentsProvideExplanation);
      break;
    }

  }

  $('input[name=studentsProvideExplanation]').on('change', function () {
    var infos = this.id.split("_");
    var sequenceId = infos[1];
    var questionType = infos[2];
    manageConfigurationChange(sequenceId, questionType, $(this))
  });

  $("input[type=radio][name='executionContext']").on('change', function () {
    var infos = this.id.split("_");
    var sequenceId = infos[1];
    var questionType = infos[2];
    manageExecutionContext(sequenceId, questionType, $(this))
  });

  $(document).ready(function () {
    $('.ui.checkbox').checkbox();
  });


</r:script>