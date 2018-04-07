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

    function abbreviate(text) {
        const maxLen = 20;
        if (text.length > maxLen) {
            text = text.substring(0, maxLen - 3) + '...';
        }

        return text;
    }

    function renderPagination(totalSongs, pageable) {
        const paginationBaseRoute = '/songs/browse/js?page=';
        const totalPages = Math.ceil(totalSongs / pageable['size']);

        let paginationList = $('.pagination');
        paginationList.css('display', 'none');
        let pageIndex = pageable['page'];
        let prevPageIndex = pageIndex - 1;
        let nextPageIndex = pageIndex + 1;
        let prevPageRoute = paginationBaseRoute + prevPageIndex;
        let nextPageRoute = paginationBaseRoute + nextPageIndex;

        let prevPageLink = $('.page-link.prev');
        let nextPageLink = $('.page-link.next');
        let prevPageItem = $('.page-item.prev');
        let nextPageItem = $('.page-item.next');
        if (prevPageIndex < 0) {
            prevPageItem.addClass('disabled');
            prevPageLink.attr('href', '#');
        } else {
            prevPageItem.removeClass('disabled');
            prevPageLink.attr('href', prevPageRoute);
        }

        if ((nextPageIndex + 1) > totalPages) {
            nextPageItem.addClass('disabled');
            nextPageLink.attr('href', '#');
        } else {
            nextPageItem.removeClass('disabled');
            nextPageLink.attr('href', nextPageRoute);
        }

        paginationList.css('display', 'flex');
    }

    function ajaxSuccess(result) {
        let jsonResult = JSON.parse(result);
        let songsAsJson = jsonResult['content'];
        renderSongs(songsAsJson);
        let totalSongs = jsonResult['total'];
        let pageable = jsonResult['pageable'];
        renderPagination(totalSongs, pageable);
    }

    function renderSongs(songsAsJson) {
        let songsContainer = $('#songsContainer');
        let paginationList = $('.pagination');
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