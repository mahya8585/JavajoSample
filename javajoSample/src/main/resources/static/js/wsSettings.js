$(function(){
    var ws = new WebSocket("ws://localhost:8080/sendUserMessage");
        ws.onopen = function(){
    };

    ws.onclose = function(){};

    ws.onmessage = function(message){
        $("#result").append(message.data).append("<br />");
    };

    ws.onerror = function(event){
        alert("接続大失敗。がびーん。");
    };

    $("#form").submit(function(){
        var messageBox = "<div class='message_box'>"
                        + "<p class='name_cs'>" + $("#name").val() + "</p>"
                        + "<div class='message_cs'>" + $("#message").val() + "</div>"

        ws.send(messageBox);
        $("#message").val("");

        //最新メッセージを表示(メッセージ直下の要素へ移動)
        setTimeout(function() {
            window.scroll(0,$(document).height());
        },0);

        return false;
    });


});