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

<div id="user-menu-dropdown"
     class="ui dropdown item user-menu"
     data-tooltip="${message(code: "layout.main.account")}"
     data-position="right center"
     data-inverted="">
  <i class="user icon"></i>

  <div class="ui vertical menu">
    <div class="header" style="text-align: center; text-overflow: ellipsis; overflow: hidden;">
      ${user ? user.fullname : sec.username()}
    </div>
    <g:link controller="userAccount"
            action="doEdit"
            class="item"><i
        class="address card outline icon"></i> ${message(code: "layout.main.account")}</g:link>

    <tsaap:ifUserOwner>
      <div class="ui divider"></div>
      <g:link
          controller="userAccountBatchCreation" class="item">
        <i class="add user icon"></i> ${message(code: "layout.main.goUserAccountCreation")}
      </g:link>
      <div class="ui divider"></div>
    </tsaap:ifUserOwner>

    <g:link controller="logout" class="item"><i
        class="sign out icon"></i> ${message(code: "layout.main.disconnect")}</g:link>
  </div>
</div>

<style>
.user-menu {
  position: relative;
}

@media (min-height: 600px) {
  .user-menu {
    position: absolute !important;
    bottom: 0 !important;
  }
}
</style>

<r:script>
  $(document)
      .ready(function () {

    // Initialize dropdown
    $('#user-menu-dropdown').dropdown();
  });
</r:script>
