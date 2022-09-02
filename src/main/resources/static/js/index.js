function searchByKeyword(){
    const type = $('#searchType').val();
    const keyword = $('#searchKeyword').val();

    let url = "?searchType=" + type + "&keyword=" + keyword;

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

    if(minPriceFilter!="") {
        url = createUrl("minPriceFilter", minPriceFilter, minChanged, url);
    }
    if(maxPriceFilter!=""){
        url = createUrl("maxPriceFilter", maxPriceFilter, maxChanged, url);
    }



    window.location.href = url;
}

function filterByReviewAvg() {
    const regex = /reviewFilter=[0-9].?[0-9]{0,}/g;
    var url = window.location.href;
    var changed =  url.match(regex);
    var reviewFilter = document.getElementById('reviewFilter').value;

    url = createUrl("reviewFilter", reviewFilter, changed, url);

    window.location.href = url;
}

function filterByCategory(category) {
    const regex = /categoryFilter=[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,}/g;
    var url = decodeURI(window.location.href);
    var changed =  url.match(regex);

    url = createUrl("categoryFilter", category, changed, url);

    window.location.href = url;
}

function orderByInput(input) {
    const regex = /sort=[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,}/g;
    var url = decodeURI(window.location.href);
    var changed =  url.match(regex);

    url = createUrl("sort", input, changed, url);

    window.location.href = url;
}

function moveToPage(num) {
    const regex = /page=[0-9]{1,}/g;
    var url = window.location.href;
    var changed = url.match(regex);

    url = createUrl("page", num, changed, url);

    window.location.href = url;
}

function createUrl(key, value, changed, url){
    if (!url.includes("?")) {
        url += "?" + key + "=" + value;
    }
    else {
        if (changed == null){
            url += "&" + key + "=" + value;
        }else{
            url = url.replace(changed, key + "=" + value);
        }
    }

    return url;
}

// 1.?가 있다면
// '?속성=' 추가

// 2.?가 없다면
// 2-1. 이미 속성에 대한 값이 있다면
// replace 진행
// 2-2. 속성에 대한 값이 없다면
// '&속성=' 추가