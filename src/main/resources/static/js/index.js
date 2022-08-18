function searchByPrice() {
    const URLSearch = new URLSearchParams(location.search);
    console.log("Here")
    console.log(URLSearch.get('page'));
    console.log(URLSearch.get('keyword'));
}