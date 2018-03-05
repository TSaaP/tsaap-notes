
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

<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; groovy.json.StringEscapeUtils" contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="elaastic-minimal">
  <title>elaastic - <g:message code="index.newUser.signUp.action"/></title>
  <r:require modules="semantic_ui,elaastic_ui,jquery,vue_js"/>
</head>

<body>

<g:set var="lang" value="${RequestContextUtils.getLocale(request)}"/>

<div id="subscribe-app" class="ui modal">
  <div class="header">
    <i class="id card icon"></i><g:message code="index.newUser.signUp.action"/> ...
  </div>

  <div class="image content">
    <div class="ui medium image">
      <img src="../images/picto/blank-profile-picture-973460_640.png">
    </div>

    <div class="description">

      <div class="ui negative message" v-if="hasServerError">
        <ul class="list">
          <li v-for="error in serverErrorList"><span v-html="error"></span></li>
        </ul>
      </div>

      <form id="subscribeForm" class="ui form">
        <div class="ui error message"></div>

        <h4 class="ui dividing header"><i class="address book icon"></i><g:message code="useraccount.personalData"/>
        </h4>

        <div class="required field">
          <label>Name</label>

          <div class="two fields">
            <div class=" field">
              <input type="text"
                     name="firstName"
                     placeholder="${message(code: "index.signUp.form.firstName.placeholder")}"
                     v-model="userData.firstName">
            </div>

            <div class="field">
              <input type="text"
                     name="lastName"
                     placeholder="${message(code: "index.signUp.form.lastName.placeholder")}"
                     v-model="userData.lastName">
            </div>
          </div>
        </div>

        <div class="required field">
          <label>Email</label>

          <div class="required field">

            <input type="text"
                   name="email"
                   placeholder="${message(code: "index.signUp.form.email.placeholder")}"
                   v-model="userData.email">

          </div>
        </div>


        <h4 class="ui dividing header"><i class="sign in icon"></i><g:message
            code="useraccount.your.elaastic.account"/></h4>

        <div class="required field">
          <label><g:message code="useraccount.profile.label"/></label>

          <div class="ui fluid selection dropdown">
            <input type="hidden" name="role">
            <i class="dropdown icon"></i>

            <div class="default text"><g:message code="useraccount.profile.select"/></div>

            <div class="menu">
              <div class="item" :data-value="ROLE.STUDENT">
                <i class="student icon"></i><g:message code="useraccount.form.learner.radio"/>
              </div>

              <div class="item" :data-value="ROLE.TEACHER">
                <i class="travel icon"></i><g:message code="useraccount.form.teacher.radio"/>
              </div>
            </div>
          </div>
        </div>

        <div class="required field">
          <label><g:message code="useraccount.username.label"/></label>

          <div class="required field">

            <input type="text"
                   name="username"
                   placeholder="${message(code: "index.signUp.form.username.placeholder")}"
                   v-model="userData.username">

          </div>
        </div>


        <div class="required field">
          <label><g:message code="useraccount.form.password.placeholder"/></label>

          <div class="two fields">
            <div class="field">
              <input type="password"
                     name="password"
                     placeholder="${message(code: "index.signUp.form.password.placeholder")}"
                     v-model="userData.password"/>
            </div>

            <div class="field">
              <input type="password"
                     name="password2"
                     placeholder="${message(code: "index.signUp.form.confirmPassword.placeholder")}"
                     v-model="userData.password2"/>
            </div>

          </div>
        </div>
      </form>

      <div class="container text">
        <div class="ui message">
          <g:if test="${lang == Locale.FRANCE || lang == Locale.FRENCH || lang == Locale.CANADA_FRENCH}">
            En vous inscrivant, vous acceptez les <a href="../terms"
                                                     target="_blank">Conditions d'utilisation</a>, notamment l'utilisation de cookies.
          </g:if>
          <g:else>
            By signing up, you agree to the <a href="../terms"
                                               target="_blank">Terms of Service</a>, including Cookie Use.
          </g:else>
        </div>
      </div>

    </div>
  </div>

  <div class="actions">
    <div class="ui deny button">
      Annuler
    </div>

    <div class="ui primary button" v-on:click="submit()" v-bind:class="{ loading: isLoading}">
      S'inscrire
    </div>
  </div>
</div>

<r:script>
%{-- Init VueJS app --}%

  var app;
  (function() {
    var ROLE = {
       STUDENT: 'STUDENT_ROLE',
       TEACHER: 'TEACHER_ROLE'
    };

    app=new Vue({
      el: '#subscribe-app',
      data: {
        serverErrorList: [],
        isLoading: false,
        userData: {
          firstName: null,
          lastName: null,
          email: null,
          role: null,
          username: null,
          password: null,
          password2: null,
          language: null
        }
      },
      ready: function() {
        alert('Hello');
      },
      methods: {
        selectRole: function(role) {
          this.userData.role = role;
        },
        submit: function() {
          var that = this;

          that.serverErrorList = [];
          var validated = $('.ui.form').form('validate form');

          if(validated) {
            that.isLoading = true;
            $.ajax({
              type: 'POST',
              url: '${g.createLink(controller: 'userAccount', action: 'ajaxDoSubscribe')}',
              data: this.userData,
              complete: function() {
                that.isLoading = false;
              },
              success: function(data) {
                if(data.success) {
                  var loginUrl = '/tsaap-notes/login/auth?justRegistered=true&skin=elaastic';
                  var checkEmail = ${checkEmail};

                  if(checkEmail) {
                    loginUrl += '&checkEmail=true';
                  }

                  window.location = loginUrl;
                }
                else {
                  that.serverErrorList = data.errorList;
                }
              },
              error: function(data) {
                that.serverErrorList = ['${groovy.json.StringEscapeUtils.escapeJavaScript(g.message(code: 'unexpected.server.error'))}']
              }
            });
          }
        }
      },
      computed: {
        ROLE: function() {
          return ROLE;
        },
        hasServerError: function() {
          return this.serverErrorList.length > 0;
        }
      }
    });
  })();



  $(document)
      .ready(function () {
    $('.ui.modal')
      .modal({
        closable: false,
        onDeny: function() {
          window.location = "${grailsApplication.config.grails.plugins.springsecurity.logout.afterLogoutUrl}";
        },
        onHide: function() {
          window.location = "${grailsApplication.config.grails.plugins.springsecurity.logout.afterLogoutUrl}";
        }
      })
      .modal('show');

    // Initialize dropdown
  $('.ui.dropdown').dropdown({
    onChange: function(value) {
      app.selectRole(value);
    }
  });

  // Initialize accordions
  $('.ui.accordion').accordion();

  $('.ui.form').form({
    on: 'blur',
    fields: {
      firstName: {
        identifier: 'firstName',
        rules:[
          {
            type: 'empty',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.firstName.mandatory'))}'
          }
        ]

      },
      lastName: {
        identifier: 'lastName',
        rules:[
          {
            type: 'empty',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.lastName.mandatory'))}'
          }
        ]

      },
      email: {
        identifier: 'email',
        rules:[
          {
            type: 'email',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.email.invalid'))}'
          }
        ]
      },
      role: {
        identifier: 'role',
        rules:[
          {
            type: 'empty',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.profile.mandatory'))}'
          }
        ]
      },
      username: {
        identifier: 'username',
        rules:[
          {
            type: 'empty',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.username.mandatory'))}'
          }
        ]
      },
      password: {
        identifier: 'password',
        rules:[
          {
            type: 'minLength[4]',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.password.minlength'))}'
          }
        ]
      },
      password2: {
        identifier: 'password2',
        rules:[
          {
            type: 'match[password]',
            prompt: '${StringEscapeUtils.escapeJavaScript(g.message(code: 'useraccount.form.password.identical'))}'
          }
        ]

      }
    }
  });

  if (navigator.browserLanguage) {
            app.userData.language = navigator.browserLanguage;
        }
        else {
            app.userData.language = navigator.languages[0].slice(0, 2);
        }

});

</r:script>

<r:style type="text/css">
%{-- Fix the description div with to fullsize --}%
  .ui.modal > .content > .icon + .description, .ui.modal > .content > .image + .description {
    flex: auto;
  }
</r:style>
</body>
</html>