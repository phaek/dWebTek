/**
 * Created by phaek on 1/29/17.
 */

//Håndtér 'Enter'-input med jQuery
$(document).keypress(function (e) {
    if (e.which == 13 && $(document.getElementById('searchfield')).is(':focus')) {
        searchBarSearch();
    }
});


//Søgefunktion med JS
function searchBarSearch() {
    if ((document).getElementById('searchfield').length !== 0) {
        document.getElementById('jumbotron_p').innerText = "Placeholder -- denne side kommer til at have produktinformation baseret på input (" + (document).getElementById('searchfield').value + ")";
    }
}