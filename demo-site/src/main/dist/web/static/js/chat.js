if (!window.WebSocket) {
    alert("the browser does not support WebSocket");
}
const socket = new WebSocket("wss://localhost:8443/ws/chat");
socket.onmessage = function (event) {
    const chat = document.getElementById("chat");
    chat.innerHTML = chat.innerHTML + event.data + "<br />";
};

socket.onclose = function (event) {
    alert(event.code + ": " + event.reason);
}

function send(message) {
    if (socket.readyState === WebSocket.OPEN) {
        const bean = {text: message};
        socket.send(JSON.stringify(bean));
    } else {
        alert("socket is not open.");
    }
    return false;
}

document.getElementById("message").onkeydown = function (event) {
    if (event.key === "Enter") {
        const input = document.getElementById("message");
        send(input.value);
        input.value = ''
    }
};

document.getElementById("form").onsubmit = function () {
    return false;
};
