$(document).ready(function() {
    // defined a connection to a new socket endpoint
    var socket = new SockJS('/endpoint');

    var stompClient = Stomp.over(socket);

    stompClient.connect({ }, function(frame) {
        // subscribe to the /topic/message endpoint
        stompClient.subscribe("/topic/message", function(data) {
            messages = JSON.parse(data.body);
            $("<tr>"
                + "<td>" + messages.message + "</td>"
                + "</tr>").prependTo("#messages");
        });
    });

    $("button#send").click(function () {
        name = $('#name').val();
        message = $('#message').val();
        if (name != "" && message != "") {
//            stompClient.send('/topic/message', {}, JSON.stringify({"message": message}));
            stompClient.send('/topic/message', {}, JSON.stringify({"message": message}));
            $('#message').val('');
        }
    });
});
