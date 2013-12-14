var container = document.getElementById("jsoneditor");
var options = {
    mode: 'tree',
    change: function() { 
    	model.newtraitmessage(editor.getText());
    }
};
var editor = new jsoneditor.JSONEditor(container, options,{"":""});
