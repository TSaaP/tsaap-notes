<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="elaastic-minimal">
  <title>elaastic-questions</title>

  <r:require modules="semantic_ui,elaastic_ui,jquery,vue_js"/>
</head>

<body>

<div id="elaastic-home">
  <div class="ui container">
    <div class="ui large top fixed hidden inverted menu" style="background-color: #4f7691;">
      <div class="header item">
        <img class="ui avatar bordered circular image logo" src="images/elaastic/logos/Elaastic_pictoRVB.png"/>
      </div>
      

      <div class="right menu">
        <tsaap:ifNotLoggedIn>
          <div class="item">
            <g:link controller="login"
                    action="auth"
                    params="[skin: 'elaastic']"
                    class="ui button">
              ${message(code: "index.newUser.logIn.action")}
            </g:link>

          </div>

          <div class="item">
            <g:link class="ui primary button" controller="userAccount" action="showSuscribeForm">
              <g:message code="index.newUser.signUp.action"/>
            </g:link>
          </div>
        </tsaap:ifNotLoggedIn>
        <tsaap:ifLoggedIn>
          <div class="ui dropdown item">
            <i class="user icon"></i> <sec:username/><i class="dropdown icon"></i>

            <div class="menu">
              <g:link controller="userAccount"
                      action="doEdit"
                      class="item"><i
                  class="address card outline icon"></i> ${message(code: "layout.main.account")}</g:link>
              <g:link controller="settings"
                      action="doSettings"
                      class="item"><i class="settings icon"></i> ${message(code: "layout.main.settings")}</g:link>
              <g:link controller="logout" class="item"><i
                  class="sign out icon"></i> ${message(code: "layout.main.disconnect")}</g:link>
            </div>
          </div>
        </tsaap:ifLoggedIn>
      </div>
    </div>
  </div>
  <!--Sidebar Menu-->
  <div class="ui vertical inverted sidebar menu">
  
    <tsaap:ifNotLoggedIn>
      <g:link controller="login"
              action="auth"
              params="[skin: 'elaastic']"
              class="item">
        ${message(code: "index.newUser.logIn.action")}
      </g:link>
      <g:link class="item" controller="userAccount" action="showSuscribeForm">
        <g:message code="index.newUser.signUp.action"/>
      </g:link>
    </tsaap:ifNotLoggedIn>
    <tsaap:ifLoggedIn>
      <div class="item">
        <div class="header"><i class="user icon"></i> <sec:username/></i></div>

        <div class="menu">
          <g:link controller="userAccount"
                  action="doEdit"
                  class="item"><i
              class="address card outline icon"></i> ${message(code: "layout.main.account")}</g:link>
          <g:link controller="settings"
                  action="doSettings"
                  class="item"><i class="settings icon"></i> ${message(code: "layout.main.settings")}</g:link>
          <g:link controller="logout" class="item"><i
              class="sign out icon"></i> ${message(code: "layout.main.disconnect")}</g:link>
        </div>
      </div>
    </tsaap:ifLoggedIn>

  </div>
  <!--Page Contents-->
  <div class="pusher">
    <div id="section-home" class="ui inverted vertical masthead center aligned segment">
      <div class="ui container">
        <div class="ui large secondary inverted pointing menu">

          <div class="right item">

            <tsaap:ifNotLoggedIn>
              <g:link controller="login"
                      action="auth"
                      params="[skin: 'elaastic']"
                      class="ui inverted button">
                ${message(code: "index.newUser.logIn.action")}
              </g:link>
              <g:link class="ui inverted button" controller="userAccount" action="showSuscribeForm">
                <g:message code="index.newUser.signUp.action"/>
              </g:link>
            </tsaap:ifNotLoggedIn>
            <tsaap:ifLoggedIn>
              <div class="ui dropdown item">
                <i class="user icon"></i> <sec:username/><i class="dropdown icon"></i>

                <div class="menu">
                  <g:link controller="userAccount"
                          action="doEdit"
                          class="item"><i
                      class="address card outline icon"></i> ${message(code: "layout.main.account")}</g:link>
                  <g:link controller="settings"
                          action="doSettings"
                          class="item"><i class="settings icon"></i> ${message(code: "layout.main.settings")}</g:link>
                  <g:link controller="logout" class="item"><i
                      class="sign out icon"></i> ${message(code: "layout.main.disconnect")}</g:link>
                </div>
              </div>
            </tsaap:ifLoggedIn>
          </div>
        </div>
      </div>

      <div class="ui container">
        <div class="introduction">
          <g:img class="ui big centered image"
                 uri="/images/elaastic/logos/elaastic_logoRVBline_inverse.png" />
        </div>

        <g:link class="ui huge primary button" controller="login" action="auth">
          Get Started<i class="right arrow icon"></i>
        </g:link>
      </div>

    </div>


  </div>

</div>

%{-- Specific homepage style --}%
<style type="text/css">
.hidden.menu {
  display: none;
}

.ui.inverted.vertical.masthead.segment,
.ui.inverted.vertical.footer.segment {
  background-color: #4f7691;
}

.ui.secondary.pointing.menu {
  border: none;
}

.masthead.segment {
  min-height: 600px;
  padding: 1em 0em;
}

.masthead .logo.item img {
  margin-right: 1em;
}

.masthead .ui.menu .ui.button {
  margin-left: 0.5em;
}

.masthead h1.ui.header {
  margin-top: 2em;
  margin-bottom: 0em;
  font-size: 4em;
  font-weight: normal;
}

.masthead h2 {
  font-size: 1.7em;
  font-weight: normal;
}

.masthead .introduction {
  height: 55vh;
  margin-left: auto !important;
  margin-right: auto !important;
  min-height: 400px;
  -webkit-box-align: center;
  -webkit-align-items: center;
  -ms-flex-align: center;
  align-items: center;
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: center;
  -webkit-justify-content: center;
  -ms-flex-pack: center;
  justify-content: center;
}

@media only screen and (max-width: 810px) {
  .masthead .introduction {
    min-height: 350px;
    height: auto;
  }
}

.ui.vertical.stripe {
  padding: 8em 0em;
}

.ui.vertical.stripe h3 {
  font-size: 2em;
}

.ui.vertical.stripe .button + h3,
.ui.vertical.stripe p + h3 {
  margin-top: 3em;
}

.ui.vertical.stripe .floated.image {
  clear: both;
}

.ui.vertical.stripe p {
  font-size: 1.33em;
}

.ui.vertical.stripe .horizontal.divider {
  margin: 3em 0em;
}

.quote.stripe.segment {
  padding: 0em;
}

.quote.stripe.segment .grid .column {
  padding-top: 5em;
  padding-bottom: 5em;
}

.footer.segment {
  padding: 5em 0em;
}

.secondary.pointing.menu .toc.item {
  display: none;
}

@media only screen and (max-width: 700px) {
  .ui.fixed.menu {
    display: none !important;
  }

  .secondary.pointing.menu .item,
  .secondary.pointing.menu .menu {
    display: none;
  }

  .secondary.pointing.menu .toc.item {
    display: block;
  }

  .masthead.segment {
    min-height: 350px;
  }

  .masthead h1.ui.header {
    font-size: 2em;
    margin-top: 1.5em;
  }

  .masthead h2 {
    margin-top: 0.5em;
    font-size: 1.5em;
  }
}
</style>



<r:script>
%{-- Init VueJS app --}%
  var app = new Vue({
      el: '#elaastic-home',
      data: {
        currentSection: ''
      },
      methods: {
        setActiveSection: function(section) {
          this.currentSection = section;
        }
      }
    });

%{-- Display the top menu once the master head has been scrolled down --}%
  $(document)
      .ready(function () {
    // Initialize dropdown
    $('.ui.dropdown').dropdown();

    // create sidebar and attach to menu open
    $('.ui.sidebar')
%{-- Technical note: it should have a context, but then the window scroll is no more detected... An issue has been submitted on Semantic-UI: https://github.com/Semantic-Org/Semantic-UI/issues/5896 --}%
  .sidebar({/*context: $('#elaastic-home'),*/ silent: true}).sidebar('attach events', '.toc.item', 'show');

  // fix menu when passed
  $('#section-home').visibility({
      once: false,
      initialCheck: true,
      onTopPassed: function () {
        app.setActiveSection('home');
      },
      onBottomPassed: function () {
        $('.fixed.menu').transition('fade in');
      },
      onBottomPassedReverse: function () {
        $('.fixed.menu').transition('fade out');
        app.setActiveSection('home');
      }
    });

    // set active menus when scrolling
    $.each(
      ['work', 'company', 'careers'],
      function(index, section) {
        $('#section-'+section).visibility({
          once: false,
          initialCheck: true,
          onTopPassed: function () {
            app.setActiveSection(section);
          },
          onBottomPassedReverse: function () {
            app.setActiveSection(section);
          }
        });
      }
    );
  });

</r:script>

</body>
</html>