<div class="panel panel-default">
    <div class="panel-heading">
        <a data-toggle="collapse"
           href="#explanationsBloc">
            <g:message code="sequence.explanation.label"/>
        </a>
    </div>

    <div class="panel-body accordion-body collapse" id="explanationsBloc">
        <div class="accordion-inner" id="teacherExplanations">
            <div id="expectedExplanationBloc">
                <label><g:message code="statement.expectedExplanation.label"/></label>
                <ckeditor:editor name="expectedExplanation" id="expectedExplanation" height="7em">
                    ${statementInstance?.expectedExplanation}
                </ckeditor:editor>
            </div>

            <div id="explanationApp">
                <div style="margin-top: 1em" v-for="(fakeExplanation,index) in fakeExplanationList">
                    <label><g:message code="statement.fakeExplanation.label"/> {{ index + 1 }}</label>
                    <button class="btn btn-default btn-xs pull-right"
                            id="removeFakeExplanationButton" type="button"
                            v-on:click="removeFakeExplanation(index)"><span
                            class="glyphicon glyphicon-remove"></span>
                    </button>
                    <textarea v-bind:name="'fakeExplanations['+index+']'" v-bind:id="'fakeExplanations['+index+']'"
                              v-model="fakeExplanation.data"></textarea>
                </div>

                <div id="fakeExplanationsBloc" style="margin-top: 1em; margin-bottom: 3em">
                    <button class="btn btn-default pull-right"
                            id="addFakeExplanationButton" type="button" v-on:click="addFakeExplanation"><span
                            class="glyphicon glyphicon-plus"></span> <g:message
                            code="statement.addFakeExplanation.label"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<r:script>
    initExplanations();

    function initExplanations() {
        var expectedExplanationContent = CKEDITOR.instances.expectedExplanation.getData();
        if (expectedExplanationContent.trim().length > 0) {
            $('#explanationsBloc').addClass('in');
        }
    }

    var explanationApp = new Vue({
        el: '#explanationApp',
        data: {
            fakeExplanationList: [],
            ckeditorInstances: []
        },

        created: function() {
            this.$http.get("/tsaap-notes/sequence/findAllFakeExplanation/${statementInstance.id}").then(response => {
                    this.fakeExplanationList = response.body
            }, response => {
                alert('An error occured contacting the server: '+response.body);
            });
        },


        updated: function () {
            this.initializeCKEditors();
            this.synchroniseModelAndCkeditorData();
            this.destroyUnusedCkeditors();
        },

        methods: {

            addFakeExplanation: function () {
                this.fakeExplanationList.push({data: 'Type a fake explanation here...'});
            },

            removeFakeExplanation: function(index) {
                this.fakeExplanationList.splice(index,1);
            },

            initializeCKEditors: function() {
                for(var i=this.ckeditorInstances.length ; i < this.fakeExplanationList.length ; i++) {
                    var currentInstance = CKEDITOR.replace('fakeExplanations[' + i +']', {
                        customConfig: '/tsaap-notes/ckeditor/config.js',
                        height: 100
                    });
                    this.ckeditorInstances.push(currentInstance);
                }
            },

            destroyUnusedCkeditors: function() {
                for(var i=this.fakeExplanationList.length ; i < this.ckeditorInstances.length ; i++) {
                    var currentInstance = this.ckeditorInstances[i];
                    try {
                        currentInstance.destroy(true);
                    } catch (e) {}
                }
                var nbOfNotUsedCkeditor = this.ckeditorInstances.length - this.fakeExplanationList.length;
                if (nbOfNotUsedCkeditor > 0) {
                    this.ckeditorInstances.splice(this.fakeExplanationList.length,nbOfNotUsedCkeditor);
                }
            },

            synchroniseModelAndCkeditorData: function() {
                for (var i= 0 ; i < this.fakeExplanationList.length ; i++) {
                        if (this.fakeExplanationList[i] !== this.ckeditorInstances[i].getData()) {
                            this.ckeditorInstances[i].setData(this.fakeExplanationList[i])
                        }
                 }
            }
        }
    });
</r:script>