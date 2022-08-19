function searchByKeyword(){
    var url = "?keyword=" + document.getElementById('keyword').value;
    alert(url);
    window.location.href = url;
}

function filterByPrice() {
    const minRegex = /minPriceFilter=[0-9]{1,}/g;
    const maxRegex = /maxPriceFilter=[0-9]{1,}/g;
    var url = window.location.href;
    var minChanged =  url.match(minRegex);
    var maxChanged =  url.match(maxRegex);
    var minPriceFilter = document.getElementById('minPriceFilter').value;
    var maxPriceFilter = document.getElementById('maxPriceFilter').value;

    if(url.indexOf("?")== -1) {
        url += "?page=1";
    }

    if(minPriceFilter != ""){
        if (minChanged == null){
            url += "&minPriceFilter=" + minPriceFilter;
        }else{
            url = url.replace(minChanged, "minPriceFilter=" + minPriceFilter);
        }
    }

    if(maxPriceFilter != ""){
        if (maxChanged == null){
            url += "&maxPriceFilter=" + maxPriceFilter;
        }else{
            url = url.replace(maxChanged, "maxPriceFilter=" + maxPriceFilter);
        }
    }


    window.location.href = url;
}

function filterByReviewAvg() {
    const regex = /reviewFilter=[0-9]{1,}/g;
    var url = window.location.href;
    var changed =  url.match(regex);
    var reviewFilter = document.getElementById('reviewFilter').value;

    if(url.indexOf("?")== -1) {
        url += "?page=1";
    }

    if(reviewFilter != ""){
        if (changed == null){
            url += "&reviewFilter=" + reviewFilter;
        }else{
            url = url.replace(changed, "reviewFilter=" + reviewFilter);
        }
    }

    window.location.href = url;
}

function filterByCategory(category) {
    const regex = /categoryFilter=[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,}/g;
    var url = decodeURI(window.location.href);
    var changed =  url.match(regex);

    if(url.indexOf("?")== -1) {
        url += "?page=1";
    }

    if (changed == null){
        url += "&categoryFilter=" + category;
    }else{
        url = url.replace(changed, "categoryFilter=" + category);
    }

    window.location.href = url;
}

function orderByInput(input) {
    const regex = /sort=[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,}/g;
    var url = decodeURI(window.location.href);
    var changed =  url.match(regex);

    if(url.indexOf("?")== -1) {
        url += "?page=1";
    }

    if (changed == null){
        url += "&sort=" + input;
    }else{
        url = url.replace(changed, "sort=" + input);
    }

    window.location.href = url;
}

function moveToPage(num) {
    const regex = /page=[0-9]{1,}/g;
    var url = window.location.href;
    var changed = url.match(regex)[0];

    url = url.replace(changed, "page="+num);

    window.location.href = url;
}기