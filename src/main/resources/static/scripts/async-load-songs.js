$('.page-link').on('click', function (e) {
    e.preventDefault();

    const songTitleKey = 'songTitle';
    let songTitle = getValByQueryParam(songTitleKey);
    let url = $(this).attr('href');
    $.ajax({
        url: url,
        data: {songTitleKey, songTitle},
        success: ajaxSuccess,
        error: ajaxError
    });

    function getValByQueryParam(songTitleKey) {
        let windowSearch = window.location.search;
        if (windowSearch) {
            let queryStr = windowSearch.split('?')[1];
            let entries = queryStr.split('&');
            for (let entry of entries) {
                let queryTokens = entry.split('=');
                if (queryTokens.length !== 2) {
                    break;
                }

                let encodedKey = queryTokens[0].replace(/\+/g, '%20');
                let encodedValue = queryTokens[1].replace(/\+/g, '%20');
                let decodedKey = decodeURIComponent(encodedKey);
                let decodedValue = decodeURIComponent(encodedValue);
                if (decodedKey === songTitleKey) {
                    return decodedValue;
                }
            }
        }
    }

    function markSelectedPage() {
        $('.page-item').removeClass('active');
        $(e.currentTarget).parent().addClass('active');
    }

    function abbreviate(text) {
        const maxLen = 20;
        if (text.length > maxLen) {
            text = text.substring(0, maxLen - 3) + '...';
        }

        return text;
    }

    function ajaxSuccess(result) {
        let jsonResult = JSON.parse(result);
        let songsAsJson = jsonResult['content'];
        renderSongs(songsAsJson);
        markSelectedPage();
    }

    function renderSongs(songsAsJson) {
        let songsContainer = $('#songsContainer');
        let paginationList = $('.pagination');
        //songsContainer.css('display', 'none');
        songsContainer.empty();
        if (songsAsJson.length === 0) {
            let noSongsAvailable = $('<div><h3>No songs available</h3></div>');
            noSongsAvailable.appendTo(songsContainer);
            songsContainer.css('display', '');
            paginationList.empty();
            return;
        }

        for (let songJson of songsAsJson) {
            let songId = songJson['id'];
            let originalSongTitle = songJson['title'];
            let songTitle = abbreviate(originalSongTitle);
            let songUploader = songJson['uploaderUsername'];
            let songInfo = $('<div class="col-md-2">');
            let figure = $('<figure></figure>');
            let image = $('<img src="/images/music.png" alt="cover" class="img-fluid">');
            image.appendTo(figure);
            let figCaption = $('<figcaption class="mt-2"></figcaption>');
            let title = $(`<h6><a class="title" 
                                  title="${originalSongTitle}" href="/songs/details/${songId}">${songTitle}</a></h6>`);
            let author = $(`<a class="author" href="/users/profile/${songUploader}" >${songUploader}</a>`);
            title.appendTo(figCaption);
            author.appendTo(figCaption);
            figCaption.appendTo(figure);
            figure.appendTo(songInfo);
            songInfo.appendTo(songsContainer);
        }
    }

    function ajaxError() {
        alert('Error while loading of songs');
    }
});