function sendMessage(inputId) {
    let data = {text: textFrom(inputId)};
    let id = getChatID(document.URL);
    fetch('/messages/', {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Chat' : id.toString()
        },
        body: JSON.stringify(data)
    }).then(() => {
        console.log('sent!');
    });
}

function getChatID(url) {
    const expression = /chat\/(\d+)/;
    let id = url.match(expression);

    if (!id) {
        return false;
    }

    return id[1];
}

function textFrom(elementId) {
    return document.getElementById(elementId).value;
}

function fetchNewMessages() {
    fetch('/messages/notification', {
        credentials: 'include'
    }).then(response => response.json())
        .then(prependMessage)
        .then(() => fetchNewMessages());
}

async function fetchHistory() {
    let id = getChatID(document.URL);
    fetch('/messages/' + id, {
        credentials: 'include'
    }).then(response => response.json())
        .then(messages => messages.forEach(prependMessage));
}

function prependMessage(message) {
    let listId = 'messages-list';
    let list = document.getElementById(listId);

    let messageView = document.createElement('li');

    messageView.innerText = `${message.dateSend.toLocaleString()}. ${message.user} said: ${message.text}`;

    list.prepend(messageView);
}
