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
CKEDITOR.plugins.addExternal( 'confighelper', '${resource(dir: '/ckeditor/plugins/confighelper/')}' );
CKEDITOR.editorConfig = function( config ) {
    config.floatSpaceDockedOffsetY = 30;
    config.extraPlugins = 'pbckcode,confighelper';

        config.pbckcode = {
        modes :  [
            ['C/C++'        , 'c_pp'],
            ['C9Search'     , 'c9search'],
            ['Clojure'      , 'clojure'],
            ['CoffeeScript' , 'coffee'],
            ['ColdFusion'   , 'coldfusion'],
            ['C#'           , 'csharp'],
            ['CSS'          , 'css'],
            ['Diff'         , 'diff'],
            ['Glsl'         , 'glsl'],
            ['Go'           , 'golang'],
            ['Groovy'       , 'groovy'],
            ['haXe'         , 'haxe'],
            ['HTML'         , 'html'],
            ['Jade'         , 'jade'],
            ['Java'         , 'java'],
            ['JavaScript'   , 'javascript'],
            ['JSON'         , 'json'],
            ['JSP'          , 'jsp'],
            ['JSX'          , 'jsx'],
            ['LaTeX'        , 'latex'],
            ['LESS'         , 'less'],
            ['Liquid'       , 'liquid'],
            ['Lua'          , 'lua'],
            ['LuaPage'      , 'luapage'],
            ['Markdown'     , 'markdown'],
            ['OCaml'        , 'ocaml'],
            ['Perl'         , 'perl'],
            ['pgSQL'        , 'pgsql'],
            ['PHP'          , 'php'],
            ['Powershell'   , 'powershel1'],
            ['Python'       , 'python'],
            ['R'            , 'ruby'],
            ['OpenSCAD'     , 'scad'],
            ['Scala'        , 'scala'],
            ['SCSS/Sass'    , 'scss'],
            ['SH'           , 'sh'],
            ['SQL'          , 'sql'],
            ['SVG'          , 'svg'],
            ['Tcl'          , 'tcl'],
            ['Text'         , 'text'],
            ['Textile'      , 'textile'],
            ['XML'          , 'xml'],
            ['XQuery'       , 'xq'],
            ['YAML'         , 'yaml']
        ],
    };

	config.toolbarGroups = [
		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
		%{--{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },--}%
		%{--{ name: 'forms', groups: [ 'forms' ] },--}%
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'paragraph' ] },
		{ name: 'insert', groups: [ 'insert' ] } ,
		%{--{ name: 'styles', groups: [ 'styles' ] },--}%
		{ name: 'colors', groups: [ 'colors' ] },
		%{--{ name: 'tools', groups: [ 'tools' ] },--}%
		%{--{ name: 'others', groups: [ 'others' ] },--}%
		%{--{ name: 'about', groups: [ 'about' ] },--}%
		{ name: 'pbckcode' }
	];

	config.removeButtons = 'Scayt,SelectAll,Find,Save,NewPage,Preview,Print,Templates,Cut,Copy,Redo,Paste,PasteText,PasteFromWord,Undo,Form,HiddenField,Radio,TextField,Checkbox,Textarea,Select,Button,ImageButton,CreateDiv,BidiLtr,BidiRtl,Language,Anchor,Flash,PageBreak,Iframe,BGColor,ShowBlocks,Maximize,About,Replace,Source,Strike,Subscript,Superscript';
	config.removePlugins = 'smiley,horizontalrule,elementspath,specialchar';
	config.skin = 'minimalist,${g.createLink(uri: '/ckeditor/skins/minimalist/')}';
};