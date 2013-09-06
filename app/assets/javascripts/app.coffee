require(["webjars!knockout.js", 'webjars!jquery.js', "/routes.js", "webjars!bootstrap.js"], (ko) ->

  /* Models for the messages page */
  class MessagesModel
    constructor: () ->
      self = @
      /* the list of messages */
      @messages = ko.observableArray()

      /* the messages field that messages are entered into */
      @messageField = ko.observable()

      /* the URL to fetch the next page of messages, if one exists */
      @nextMessagesUrl = ko.observable()

      /* the URL to fetch the previous page of messages, if one exists */
      @prevMessagesUrl = ko.observable()

      /* save a new message */
      @saveMessage = () ->
        @ajax(routes.controllers.MessageController.saveMessage(), {
          data: JSON.stringify({message: @messageField()})
          contentType: "application/json"
        }).done(() ->
          $("#addMessageModal").modal("hide")
          self.messageField(null)
        )

      /* get the messages */
      @getMessages = () ->
        @ajax(routes.controllers.MessageController.getMessages()).done((data, status, xhr) ->
          self.loadMessages(data, status, xhr)
        )

      /* get the next page of messages */
      @nextMessages = () ->
        if @nextMessagesUrl()
          $.ajax({url: @nextMessagesUrl()}).done((data, status, xhr) ->
            self.loadMessages(data, status, xhr)
          )

      /* get the previous page of messages */
      @prevMessages = () ->
        if @prevMessagesUrl()
          $.ajax({url: @prevMessagesUrl()}).done((data, status, xhr) ->
            self.loadMessages(data, status, xhr)
          )

    ajax: (route, params) ->
      $.ajax($.extend(params, route))

    loadMessages: (data, status, xhr) ->
      @messages(data)
      link = xhr.getResponseHeader("Link")
      if link
        next = /.*<([^>]*)>; rel="next".*/.exec(link)
        if next
          @nextMessagesUrl(next[1])
        else
          @nextMessagesUrl(null)
        prev = /.*<([^>]*)>; rel="prev".*/.exec(link)
        if prev
          @prevMessagesUrl(prev[1])
        else
          @prevMessagesUrl(null)
      else
        @nextMessagesUrl(null)
        @prevMessagesUrl(null)



  model = new MessagesModel
  ko.applyBindings(model)
  model.getMessages()

  /* Server Sent Events handling */
  events = new EventSource(routes.controllers.MainController.events().url)
  events.addEventListener("message", (e) ->
    model.messages.unshift(JSON.parse(e.data))
  , false)
)