if (!window.WebSocket) {
    alert("the browser does not support WebSocket");
}
const socket = new WebSocket("wss://localhost:8443/ws/chat");
socket.onmessage = function (event) {
    const chat = document.getElementById("chat");
    chat.innerHTML = chat.innerHTML + event.data + "<br />";
};

function send(message) {
    if (socket.readyState === WebSocket.OPEN) {
        socket.send(message);
    } else {
        alert("socket is not open.");
    }
    return false;
}

document.getElementById("message").onkeypress = function () {
    if (event.keyCode === 13) {
        const input = document.getElementById("message");
        send(input.value);
        input.value = ''
    }
};

document.getElementById("form").onsubmit = function () {
    return false;
};
