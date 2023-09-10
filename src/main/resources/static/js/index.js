const xhr = new XMLHttpRequest();
xhr.open("get", "/api/show-messages.json")
xhr.onload = function () {
    console.log(this.status)
    console.log(this.responseText)
    const messageList = JSON.parse(this.responseText);
    const oTbody = document.querySelector("tbody");
    for (const message of messageList) {
        let html = "<tr>" +
            `<td>${message.username}</td>`+
            `<td>${message.whom}</td>`+
            `<td>${message.what}</td>`+
            "</tr>"
        oTbody.innerHTML += html
    }
}
xhr.send()