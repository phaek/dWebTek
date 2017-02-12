/**
 * Handles any 'Enter' keypresses if Searchfield is focused
 */
$(document).keypress(function (e) {
    if (e.which == 13 && $(document.getElementById('searchfield')).is(':focus')) {
        searchBarSearch();
    }
});


/**
 * Populates a <div> from Search input
 */
function searchBarSearch() {
    if ((document).getElementById('searchfield').length !== 0) {
        document.getElementById('jumbotron_p').innerText = "Placeholder -- denne side kommer til at have produktinformation baseret på input (" + (document).getElementById('searchfield').value + ")";
    }
}