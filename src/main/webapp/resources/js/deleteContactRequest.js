var xhr = new XMLHttpRequest();

function deleteContactRequest(url, element) {
    xhr.onreadystatechange = function() {
        if (this.readyState !== 4) return;
        if (this.status !== 200) {
            alert( i18n.error + ': ' + (this.status ? this.statusText : i18n.reqFail) );
            return;
        }
        document.querySelector(".tbody").removeChild(element.parentNode.parentNode);
    };

    xhr.open("DELETE", url, true);
    xhr.send();
}