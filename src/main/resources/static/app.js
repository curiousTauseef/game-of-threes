var ws;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#playermessage").html("");
}

function connect() {
    ws = new WebSocket('ws://localhost:8080/game-of-three');
    ws.onmessage = function (data) {
        showPlayerMessage(data.data);
    }
    setConnected(true);
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    if ($("#name").val() >= 3 || $("#name").val().length==0) {
        var data = JSON.stringify({'number': $("#name").val()})
        ws.send(data);
    }
}

function showPlayerMessage(message) {
    if (message.includes("Won")) {
        $("#playermessage").append("<tr><td><div class=\"alert alert-info\" role=\"alert\"> " + message + " </div></td></tr>");
    } else {
        $("#playermessage").append("<tr><td> " + message + "</td></tr>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});

