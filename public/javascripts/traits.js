var Trait = function(id, name, message) {
    this.id = id;
    this.name = name;
    this.message = message;
}

var TraitViewModel = function(traits) {
    var self = this;

	this.messages = ko.observable(traits);
	this.newtraitid = ko.observable("-1");
	this.newtraitname = ko.observable("");
	this.newtraitmessage = ko.observable("");

    this.updateMessage = function(m) {self.newtraitmessage(m);}
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
    			{ data: JSON.stringify({
    			id:self.newtraitid(),
    			kind:"trait",
    			name:self.newtraitname(), message:self.newtraitmessage()}),
    			  contentType: "application/json"
			    }).done( function() {
			    	  traitmodel.getMessages();
				      $("#addMessageModal").modal("hide");
				      self.newtraitid("-1");
				      self.newtraitname("");
				      self.newtraitmessage("");
                      traiteditor.set({"":""});
    			});
    };

    this.deleteMessage = function(i) {
    	routes.controllers.MessageController.deleteMessage().ajax(
    			{ data: JSON.stringify({id:i}), 
    			  contentType: "application/json"
			    }).done( function() {
			    	traitmodel.getMessages();
    			});
    };

    this.editMessage = function(id,name,message) {
        this.newtraitid(id);
        this.newtraitname(name);
        this.newtraitmessage(message);
        traiteditor.setText(message);
    };

    this.clear = function() {
         self.newtraitid("-1");
         self.newtraitname("");
         self.newtraitmessage("");
         traiteditor.set({"":""});
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

var traitmodel = new TraitViewModel()
ko.applyBindings(traitmodel,document.getElementById("traits"));
traitmodel.getMessages()


// CARDS \\

var CardViewModel = function(cards) {
    var self = this;

    this.trait = ko.observable("anything");

	this.messages = ko.observable(cards);
	this.newtraitid = ko.observable("-1");
	this.newtraitname = ko.observable("");
	this.newtraitmessage = ko.observable("");

    this.updateMessage = function(m) {self.newtraitmessage(m);}
	this.getMessages = function(data) {
	    self.trait(data.name)
//		routes.controllers.MessageController.getCards(_id.$oid).ajax().done(
		routes.controllers.MessageController.getMessages().ajax().done(
				function(data, status, xhr) {
					self.loadMessages(data, status, xhr);
				})
	};

	this.loadMessages = function (data, status, xhr) {
        self.messages(data);
	};

    this.saveMessage = function() {
            console.log("A1");
    	//self.newtraitmessage(editor.getText());
    	routes.controllers.MessageController.saveMessage().ajax(
    			{ data: JSON.stringify({
    			id:self.newtraitid(),
    			kind:"card",
    			name:self.newtraitname(), message:self.newtraitmessage()}),
    			  contentType: "application/json"
			    }).done( function() {
			            console.log("A2");

			    	  traitmodel.getMessages();
				      $("#addMessageModal").modal("hide");
    			});
    };

    this.deleteMessage = function(i) {
    	routes.controllers.MessageController.deleteMessage().ajax(
    			{ data: JSON.stringify({id:i}),
    			  contentType: "application/json"
			    }).done( function() {
			    	traitmodel.getMessages();
    			});
    };

    this.editMessage = function(id,name,message) {
        this.newtraitid(id);
        this.newtraitname(name);
        this.newtraitmessage(message);
        traiteditor.setText(message);
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

var cardmodel = new CardViewModel();
ko.applyBindings(cardmodel,document.getElementById("cards"));
//cardmodel.getMessages()

$("#addMessageModal").on('hidden.bs.modal', traitmodel.clear);


//var CardViewModel = function(traits) {
//    var self = this;
//
//	this.cardid = ko.observable("-1");
//	this.cardname = ko.observable("");
//	this.cardmessage = ko.observable("");
//
//    this.updateMessage = function(m) {self.cardmessage(m);}
//
//    this.editMessage = function(id,name,message) {
//        this.cardid(id);
//        this.cardname(name);
//        this.cardmessage(message);
//        cardeditor.setText(message);
//    };
//};
//
//var cardmodel = new CardViewModel()
//ko.applyBindings(cardmodel,document.getElementById("cards"));
//}

// https://github.com/twbs/bootstrap/issues/9855

