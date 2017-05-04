<div class="panel panel-default" xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
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
                <div style="margin-top: 1em" v-for="index in fakeExplanationList.length">
                    <template v-if="fakeExplationIsNotNull(index-1)">
                    <button class="btn btn-default btn-xs pull-right"
                            id="removeFakeExplanationButton" type="button"
                            v-on:click="removeFakeExplanation(index-1)"><span
                            class="glyphicon glyphicon-remove"></span>
                    </button>
                    <template v-if="isOpenEnded">
                        <label><g:message code="statement.fakeExplanation.label"/> {{ index }}</label>
                    </template>
                    <template v-else="isOpenEnded">
                        <label><g:message code="statement.fakeExplanation.correspondingItem"/></label>
                        <select v-model="fakeExplanationList[index-1].correspondingItem"
                                v-bind:name="'fakeExplanations['+(index-1)+'].correspondingItem'">
                            <option v-for="choiceIndex in itemCount" v-bind:value="choiceIndex">
                                {{ choiceIndex }}
                            </option>
                        </select>
                    </template>
                    <vue-ckeditor v-bind:name="'fakeExplanations['+(index-1)+'].content'"
                                  v-model="fakeExplanationList[index-1].content"
                                  v-bind:config="ckeditorConfig"
                                    v-on:shouldPreventInfiniteLoop="shouldPreventInfiniteLoop()"></vue-ckeditor>
                    </template>
                </div>

                <div id="fakeExplanationsBloc" style="margin-top: 1em; margin-bottom: 3em">
                    <button class="btn btn-default pull-right"
                            id="addFakeExplanationButton" type="button" v-on:click="addFakeExplanation()"><span
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

    var editorCounter = 0;

    Vue.component('vue-ckeditor', {
        template: '<textarea v-bind:id="id" v-bind:value="value"></textarea>',
        props: {
            value: {
                type: String
            },
            id: {
                type: String,
                default: () => 'editor-' + (++editorCounter)
            },
            config: {
                type: Object,
                default: {}
            }
        },
        computed: {
            instance: function() {
                return CKEDITOR.instances[this.id];
            }
         },
         beforeUpdate: function() {
            if (this.value !== this.instance.getData()) {
              this.instance.setData(this.value);
            }
        },
        mounted: function() {
            CKEDITOR.replace(this.id, this.config);
            this.instance.on('change', () => {
                let html = this.instance.getData();
                if (html !== this.value) {
                    this.$emit('input', html)
                }
            });
        },
        beforeDestroy: function() {
            try {
                this.instance.destroy();
            } catch(e) {}
            this.instance = null;
        },


    });

    var explanationApp = new Vue({
        el: '#explanationApp',
        data: {
            fakeExplanationList: [],
            statementId: "${statementInstance?.id}",
            itemCount: parseInt($('#itemCount').val()),
            isOpenEnded: $('#radioOpenEnded:checked').length > 0,
            ckeditorConfig: {
                customConfig: '/tsaap-notes/ckeditor/config.js',
                height: 100
            }
        },

        created: function() {
            if (this.statementId) {
                this.$http.get('/tsaap-notes/sequence/findAllFakeExplanation/'+this.statementId).then(function(response)  {
                    this.fakeExplanationList = response.body
                }, function(response) {
                    alert('An error occured contacting the server: '+response.body);
                });
            }
        },

        beforeUpdate: function() {
          this.isOpenEnded = $('#radioOpenEnded:checked').length > 0 ;
          this.itemCount = parseInt($('#itemCount').val());
        },

        methods: {

            addFakeExplanation: function() {
                this.fakeExplanationList.push({content: 'Type a fake explanation here...', correspondingItem: 1});
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