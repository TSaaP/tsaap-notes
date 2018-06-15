/*
 *
 *  Copyright (C) 2017 Ticetime
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Affero General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Affero General Public License for more details.
 *
 *      You should have received a copy of the GNU Affero General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * @author jtranier
 */

var elaastic = elaastic || {};

elaastic.renderGraph = function(elViewSelector, choiceSpecification, results, userChoiceList, i18n) {
  i18n = i18n || {
    percentageOfVoters: 'percentage of voters',
    choice: 'choice',
    noAnswer: 'none'
  }

  if(!_.isEmpty(results)) {
    var nbItem = choiceSpecification.itemCount
    var correctIndexList = []
    _.each(
      choiceSpecification.expectedChoiceList,
      choice => correctIndexList.push(choice.index)
    )

    var graphData = []
    var hasSecondAttempt = !(typeof results[2] === 'undefined')

    _.each([1, 2],
      attempt => {
        _.times(
          nbItem,
          i => {
            var isCorrect = _.contains(correctIndexList, i+1)
            results[attempt] && results[attempt][i+1] != undefined && graphData.push({
              choice: i+1,
              value: results[attempt][i+1],
              isCorrect: isCorrect,
              color: isCorrect+'-'+attempt,
              attempt: attempt
            })
          }
        )

        if((results[1] && results[1][0]) || (results[2] && results[2][0])) {
          graphData.push({
            choice: 'ø',
            value: results[attempt][0],
            noResponse: true,
            attempt: attempt
          })
        }
      }
    )
    
    var userChoiceListData = _.collect(
      userChoiceList,
      choice => {
        var isCorrect = _.contains(correctIndexList, choice)
        return {
          value: choice,
          isCorrect: isCorrect
        }
      }
    );

    var preferredWidth = nbItem * 75 * (hasSecondAttempt ? 1.75 : 1)
    var vegaView = $(elViewSelector)
    function computeMaxWidth() { return vegaView.width() - 25; }

    function computeWidth() {
      return Math.min(preferredWidth, computeMaxWidth())
    }

    var horizontalSpec = {
      signals: [
        {
          name: 'correctedWidth',
          update: 'width - 20'
        }
      ],
      'scales': [
        {
          'name': 'yscale',
          'type': 'band',
          'domain': {'data': 'table', 'field': 'choice'},
          'range': 'height',
          'padding': 0.3,
          'round': true
        },
        {
          'name': 'xscale',
          'domain': [0, 100],
          'nice': true,
          'range': [0, {signal: 'correctedWidth'}]
        },
        {
          name: 'correct-color',
          type: 'ordinal',
          domain: [1, 2],
          "range": ['#016936', '#a6d96a']
        },
        {
          name: 'incorrect-color',
          type: 'ordinal',
          domain: [1, 2],
          "range": ['#b03060', '#fdae61']
        },
        {
          name: 'noAnswer-color',
          type: 'ordinal',
          domain: [1, 2],
          "range": ['gold', '#fff3b2']
        }
      ],

      'axes': [
        {
          'orient': 'bottom',
          'scale': 'xscale',
          grid: true,
          values: [0, 25, 50, 75, 100],
          title: i18n.percentageOfVoters
        },
        {
          'orient': 'left',
          'scale': 'yscale',
          title: i18n.choice
        }
      ],

      'marks': [
        {
          type: 'symbol',
          from: {data: 'userChoiceList'},
          encode: {
            enter: {
              shape: {value: 'circle'},
              size: {value: 300},
              'stroke':[
                {
                  test: 'datum.isCorrect',
                  value: '#016936'
                },
                {
                  value: '#b03060'
                }
              ],
              fill: [
                {
                  test: 'datum.isCorrect',
                  value: '#016936'
                },
                {
                  value: '#b03060'
                }
              ],
              fillOpacity: {value: 0.25},
              'y': {'scale': 'yscale', 'field': 'value', band: 0.5},
              'x': {'scale': 'xscale', 'value': 0, offset: -11},
              zindex: {value: 1}
            }
          }
        },
        {
          type: 'group',
          from: {
            facet: {
              data: 'table',
              name: 'facet',
              groupby: 'choice'
            }
          },
          encode: {
            enter: {
              y: {
                scale: 'yscale',
                field: 'choice'
              }
            }
          },
          signals: [
            {
              name: 'height',
              update: 'bandwidth(\'yscale\')'
            },
            {
              'name': 'tooltip',
              'value': {},
              'on': [
                {'events': 'rect:mouseover', 'update': 'datum'},
                {'events': 'rect:mouseout', 'update': '{}'}
              ]
            }
          ],
          scales: [
            {
              name: 'pos',
              type: 'band',
              range: 'height',
              domain: {
                data: 'facet',
                field: 'attempt'
              }
            }
          ],
          marks: [
            {
              'type': 'rect',
              'from': {'data': 'facet'},
              'encode': {
                'enter': {
                  'y': {'scale': 'pos', 'field': 'attempt', offset: 1},
                  'height': {'scale': 'pos', 'band': 1, offset: -2},
                  'x': {'scale': 'xscale', 'field': 'value'},
                  'x2': {'scale': 'xscale', 'value': 0}
                },
                'update': {
                  'fill':
                    [
                      {
                        test: 'datum.noResponse',
                        scale: 'noAnswer-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'datum.isCorrect',
                        scale: 'correct-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'!datum.isCorrect',
                        scale: 'incorrect-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        value: 'blue'
                      }
                    ]
                }
              }
            },
            {
              'type': 'rect',
              'from': {'data': 'facet'},
              'encode': {
                'enter': {
                  'y': {'scale': 'pos', 'field': 'attempt', offset: 1},
                  'height': {'scale': 'pos', 'band': 1, offset: -2},
                  'x': {'scale': 'xscale', 'value': 0, offset: -2},
                  'x2': {'scale': 'xscale', 'value': 0, offset: -5},
                  opacity: {value: 0.75},
                  'fill':
                    [
                      {
                        test: 'datum.noResponse',
                        scale: 'noAnswer-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'datum.isCorrect',
                        scale: 'correct-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'!datum.isCorrect',
                        scale: 'incorrect-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        value: 'yellow'
                      }
                    ]
                }
              }
            },
            {
              'type': 'text',
              'from': {'data': 'facet'},
              'encode': {
                'enter': {
                  'align': {'value': 'left'},
                  'baseline': {'value': 'middle'},
                  'fill': {'value': 'black'},
                  fontWeight: {value: 'bold'},
                  y: {'scale': 'pos', 'field': 'attempt', 'band': 0.5},
                  x: {'scale': 'xscale', 'field': 'value', 'offset': 2},
                  text: {field: 'labelValue'}
                }
              }
            }
          ]
        }
      ]
    }

    var verticalSpec = {
      'scales': [
        {
          'name': 'xscale',
          'type': 'band',
          'domain': {'data': 'table', 'field': 'choice'},
          'range': 'width',
          'padding': 0.3,
          'round': true
        },
        {
          'name': 'yscale',
          'domain': [0, 100],
          'nice': true,
          'range': 'height'
        },
        {
          name: 'correct-color',
          type: 'ordinal',
          domain: [1, 2],
          "range": ['#016936', '#a6d96a']
        },
        {
          name: 'incorrect-color',
          type: 'ordinal',
          domain: [1, 2],
          "range": ['#b03060', '#fdae61']
        },
        {
          name: 'noAnswer-color',
          type: 'ordinal',
          domain: [1, 2],
          "range": ['gold', '#fff3b2']
        }
      ],

      'axes': [
        {
          'orient': 'bottom',
          'scale': 'xscale',
          title: i18n.choice
        },
        {
          'orient': 'left',
          'scale': 'yscale',
          grid: true,
          values: [0, 25, 50, 75, 100],
          title: i18n.percentageOfVoters
        }
      ],

      'marks': [
        {
          type: 'symbol',
          from: {data: 'userChoiceList'},
          encode: {
            enter: {
              shape: {value: 'circle'},
              size: {value: 300},
              'stroke':[
                {
                  test: 'datum.isCorrect',
                  value: '#016936'
                },
                {
                  value: '#b03060'
                }
              ],
              fill: [
                {
                  test: 'datum.isCorrect',
                  value: '#016936'
                },
                {
                  value: '#b03060'
                }
              ],
              fillOpacity: {value: 0.25},
              'x': {'scale': 'xscale', 'field': 'value', band: 0.5},
              'y': {'scale': 'yscale', 'value': 0, offset: 12},
              zindex: {value: 1}
            }
          }
        },
        {
          type: 'group',
          from: {
            facet: {
              data: 'table',
              name: 'facet',
              groupby: 'choice'
            }
          },
          encode: {
            enter: {
              x: {
                scale: 'xscale',
                field: 'choice'
              }
            }
          },
          signals: [
            {
              name: 'width',
              update: 'bandwidth(\'xscale\')'
            },
            {
              'name': 'tooltip',
              'value': {},
              'on': [
                {'events': 'rect:mouseover', 'update': 'datum'},
                {'events': 'rect:mouseout', 'update': '{}'}
              ]
            }
          ],
          scales: [
            {
              name: 'pos',
              type: 'band',
              range: 'width',
              domain: {
                data: 'facet',
                field: 'attempt'
              }
            }
          ],
          marks: [
            {
              'type': 'rect',
              'from': {'data': 'facet'},
              'encode': {
                'enter': {
                  'x': {'scale': 'pos', 'field': 'attempt', offset: 3},
                  'width': {'scale': 'pos', 'band': 1, offset: -6},
                  'y': {'scale': 'yscale', 'field': 'value'},
                  'y2': {'scale': 'yscale', 'value': 0}
                },
                'update': {
                  'fill':
                    [
                      {
                        test: 'datum.noResponse',
                        scale: 'noAnswer-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'datum.isCorrect',
                        scale: 'correct-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'!datum.isCorrect',
                        scale: 'incorrect-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        value: 'yellow'
                      }
                    ]
                }
              }
            },
            {
              'type': 'rect',
              'from': {'data': 'facet'},
              'encode': {
                'enter': {
                  'x': {'scale': 'pos', 'field': 'attempt', offset: 3},
                  'width': {'scale': 'pos', 'band': 1, offset: -6},
                  'y': {'scale': 'yscale', 'value': 0, offset: 2},
                  'y2': {'scale': 'yscale', 'value': 0, offset: 5},
                  zindex: {value: 0},
                  opacity: {value: 0.75},
                  'fill':
                    [
                      {
                        test: 'datum.noResponse',
                        scale: 'noAnswer-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'datum.isCorrect',
                        scale: 'correct-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        test:'!datum.isCorrect',
                        scale: 'incorrect-color',
                        data: 'table',
                        'field': 'colorIndex'
                      },
                      {
                        value: 'yellow'
                      }
                    ]

                }
              }
            },
            {
              'type': 'text',
              'from': {'data': 'facet'},
              'encode': {
                'enter': {
                  'align': {'value': 'center'},
                  'baseline': {'value': 'bottom'},
                  'fill': {'value': '#333'},
                  fontWeight: {value: 'bold'},

                  'x': {'scale': 'pos', 'field': 'attempt', 'band': 0.5},
                  'y': {'scale': 'yscale', 'field': 'value', 'offset': -2},
                  'text': {'field': 'labelValue'},
                }
              }
            }
          ]
        }
      ]
    }

    var orientedSpec =
      (nbItem > 5 || computeMaxWidth() < 300) ?
        horizontalSpec : verticalSpec

    var spec = {
      '$schema': 'https://vega.github.io/schema/vega/v4.json',
      'width': computeWidth(),
      'height': 200,
      'padding': 5,

      'data': [
        {
          'name': 'table',
          'values': graphData,
          transform: [
            {
              type: 'formula',
              as: 'labelValue',
              expr: 'round(datum.value) + \'%\''
            },
            {
              type: 'joinaggregate',
              fields: ['attempt'],
              ops: ['max'],
              as: ['nbAttempt']
            },
            {
              type: 'formula',
              as: 'colorIndex',
              expr: 'datum.nbAttempt - datum.attempt + 1'
            }
          ]
        },
        {
          name: 'userChoiceList',
          values: userChoiceListData
        }
      ],

      signals: orientedSpec.signals,

      'scales': orientedSpec.scales,

      'axes': orientedSpec.axes,

      'marks': orientedSpec.marks
    };

    var view;

    render(spec)

    function render (spec) {
      view = new vega.View(vega.parse(spec))
        .renderer('canvas')  // set renderer (canvas or svg)
        .initialize(elViewSelector) // initialize view within parent DOM container
        .hover()             // enable hover encode set processing
        .run();


      $(window).on('resize', function() {
        view.signal('width', computeWidth()).run('enter')
      })
    }
  }
}