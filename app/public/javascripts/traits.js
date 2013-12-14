var Trait = function(id, name, message) {
    this.id = id;
    this.name = name;
    this.message = message;
}

var TraitViewModel = function(traits) {
    var self = this;

	this.messages = ko.observable(traits);
	this.newtraitname = ko.observable();	
	this.newtraitmessage = ko.observable();

	this.getMessages = function() {
		routes.controllers.MessageController.getMessages().ajax().done( 
				function(data, status, xhr) {
					self.loadMessages(data, status, xhr);
				})
	};
	
	this.loadMessages = function (data, status, xhr) {
        self.messages(data);
	};

    this.saveMessage = function() {
    	//self.newtraitmessage(editor.getText());
    	routes.controllers.MessageController.saveMessage().ajax(
    			{ data: JSON.stringify({name:self.newtraitname(), message:self.newtraitmessage()}), 
    			  contentType: "application/json"
			    }).done( function() {
			    	  model.getMessages();
				      $("#addMessageModal").modal("hide");
				      self.newtraitname(null);
				      self.newtraitmessage(null);
              editor.set({"":""});
    			});
    };

    this.deleteMessage = function(i) {
    	routes.controllers.MessageController.deleteMessage().ajax(
    			{ data: JSON.stringify({id:i}), 
    			  contentType: "application/json"
			    }).done( function() {
			    	model.getMessages();
    			});
    };

    /*

          # Server Sent Events handling
          events = new EventSource(routes.controllers.MainController.events().url)
          events.addEventListener("message", (e) ->
            # Only add the data to the list if we're on the first page
            if model.prevMessagesUrl() == null
              message = JSON.parse(e.data)
              model.messages.unshift(message)
              # Keep messages per page limit
              if model.messages().length > messagesPerPage
                model.messages.pop()
          , false)
*/
};

var model = new TraitViewModel()
ko.applyBindings(model);
model.getMessages()
