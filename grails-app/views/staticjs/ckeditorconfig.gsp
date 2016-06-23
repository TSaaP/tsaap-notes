%{--
  - Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
  -
  -     This program is free software: you can redistribute it and/or modify
  -     it under the terms of the GNU Affero General Public License as published by
  -     the Free Software Foundation, either version 3 of the License, or
  -     (at your option) any later version.
  -
  -     This program is distributed in the hope that it will be useful,
  -     but WITHOUT ANY WARRANTY; without even the implied warranty of
  -     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -     GNU Affero General Public License for more details.
  -
  -     You should have received a copy of the GNU Affero General Public License
  -     along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --}%
<%@ page contentType="text/javascript;charset=UTF-8" %>
CKEDITOR.plugins.addExternal( 'pbckcode', '${resource(dir: '/ckeditor/plugins/pbckcode/')}' );
CKEDITOR.editorConfig = function( config ) {
    config.extraPlugins = 'pbckcode';
        config.pbckcode = {
        modes :  [
            ['Java' , 'java'],
            ['Markdown' , 'markdown']
        ],
    };

	config.toolbarGroups = [
		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
		{ name: 'forms', groups: [ 'forms' ] },
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
		{ name: 'links', groups: [ 'links' ] },
		{ name: 'insert', groups: [ 'insert' ] },
		{ name: 'styles', groups: [ 'styles' ] },
		{ name: 'colors', groups: [ 'colors' ] },
		{ name: 'tools', groups: [ 'tools' ] },
		{ name: 'others', groups: [ 'others' ] },
		{ name: 'about', groups: [ 'about' ] },
		{ name: 'pbckcode' }
	];

	config.removeButtons = 'Scayt,SelectAll,Find,Save,NewPage,Preview,Print,Templates,Cut,Copy,Redo,Paste,PasteText,PasteFromWord,Undo,Form,HiddenField,Radio,TextField,Checkbox,Textarea,Select,Button,ImageButton,CreateDiv,BidiLtr,BidiRtl,Language,Anchor,Flash,PageBreak,Iframe,BGColor,ShowBlocks,Maximize,About,Replace';
};