function saveContactRequest() {
    var data = JSON.stringify({
        name:        document.querySelector("#name").value,
        image:       document.querySelector("#image").value,
        description: document.querySelector("#description").value,
        phone:       document.querySelector("#phone").value
    }) ;
    // var data = document.querySelector(".contact-form").serialize();
    xhr.onreadystatechange = function() {
        if (this.readyState !== 4) return;

        if (this.status !== 200 ) {
            alert(i18n.Error + ': ' + (this.status ? this.statusText : i18n.reqFail) );
            return;
        }
        xhr.responseText === "true" ? alert(i18n.success) : alert(i18n.invInp);
    };
    xhr.open(method, requestURL, true);
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(data);
}