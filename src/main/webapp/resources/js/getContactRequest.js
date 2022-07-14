var xhr = new XMLHttpRequest();

function getContactRequest(id) {
    if(id) {
        xhr.onreadystatechange = function () {
            if (this.readyState !== 4) return;
            if (this.status !== 200) {
                alert(i18n.error + ': ' + (this.status ? this.statusText : i18n.reqFail));
                return;
            }
            var resp = JSON.parse(xhr.responseText);
            document.querySelector("#name").value = resp.name;
            document.querySelector("#image").value = resp.image;
            document.querySelector("#description").innerHTML = resp.description;
            document.querySelector("#phone").value = resp.phone;
        };

        xhr.open("GET", "/contacts/" + id + "/get", true);
        xhr.send();
    }
}