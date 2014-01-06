function createEditor(editorname,text,update) {
    var container = document.getElementById(editorname);
    var options = {
        mode: 'tree',
        change: update
    };
    var editor = new jsoneditor.JSONEditor(container, options, text);
    return editor;
}