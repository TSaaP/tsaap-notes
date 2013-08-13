%{--
  - Copyright 2013 Tsaap Development Group
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%



<html>
<head>
  <meta name="layout" content="main"/>
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>
<div class="container note-edition">
  <form>
    <textarea class="form-control" rows="3" id="note_field"
              maxlength="280"></textarea>
    <span id="character_counter"></span><button type="submit"
                                                class="btn btn-primary btn-xs pull-right"><span
            class="glyphicon glyphicon-plus"></span> Add note</button>
  </form>
</div>

<div class="divider"></div>

<div class="container note-list">
  <form>
    <label class="checkbox-inline">
      <input type="checkbox" id="displays_my_notes" value="displays_my_notes" checked="checked"> My notes
    </label>
    <label class="checkbox-inline">
      <input type="checkbox" id="displays_my_favorite" value="displays_my_favorite"> My favorites
    </label>
    <label class="checkbox-inline">
      <input type="checkbox" id="displays_other" value="displays_other"> Others
    </label>
  </form>
  <ul class="list-group">

    <li class="list-group-item">
      <h5 class="list-group-item-heading"><strong>${user.fullname}</strong> @<sec:username/>
      </h5>

      <p>Ceci est une prise de note avec des #tags, et des @mentions</p>
    </li>

  </ul>
</div>

<script type="text/javascript">
  jQuery(document).ready(function ($) {

    // Get the textarea field
    $('#note_field')

      // Bind the counter function on keyup and blur events
            .bind('keyup blur', function () {
                    // Count the characters and set the counter text
                    $('#character_counter').text($(this).val().length + '/280 characters');
                  })

      // Trigger the counter on first load
            .keyup();
  });
</script>
</body>
</html>