/*
 * Copyright (C) 2017 Ticetime
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author Franck Silvestre
 * @author John Tranier
 */
let editorCounter = 0;

Vue.component('vue-ckeditor', {
  template: '<textarea v-bind:id="id" v-bind:value="value" v-bind:placeholder="placeholder"></textarea>',
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
    },
    placeholder: {
      value: {
        type: String
      }
    }
  },
  computed:
    {
      instance: function () {
        return CKEDITOR.instances[this.id];
      }
    }
  ,
  beforeUpdate: function () {
    if (this.value !== this.instance.getData()) {
      this.instance.setData(this.value);
    }
  }
  ,
  mounted: function () {
    let that = this;

    CKEDITOR.inline(this.id, this.config);

    this.instance.on('instanceReady', function (evt) {
      let editor = evt.editor;

      editor.on('focus', function (e) {
        that.$emit('focus')
      });

      editor.on('blur', function (e) {
        that.$emit('blur')
      });

      editor.on('change', function () {
        let html = editor.getData();
        if (html !== that.value) {
          that.$emit('input', html)
        }
      });

    });
  }
  ,
  beforeDestroy: function () {
    try {
      this.instance.destroy();
    } catch (e) {
    }
    this.instance = null;
  }


})
;
