$('.page-link.song').on('click', function (e) {
    e.preventDefault();
    let url = $(this).attr('href');
    $.ajax({
        url: url,
        success: ajaxSuccess,
        error: ajaxError
    });

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
        songsContainer.css('display', 'none');
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
            let songTitle = abbreviate(songJson['title']);
            let songUploader = songJson['uploaderUsername'];
            let songInfo = $('<div class="col-md-2">');
            let figure = $('<figure></figure>');
            let image = $('<img src="/images/music.png" alt="cover" class="img-fluid">');
            image.appendTo(figure);
            let figCaption = $('<figcaption class="mt-2"></figcaption>');
            let title = $(`<h6><a class="title" 
                                    href="/songs/details/${songId}">${songTitle}</a></h6>`);
            let author = $(`<a class="author" href="/users/profile/${songUploader}">${songUploader}</a>`);
            title.appendTo(figCaption);
            author.appendTo(figCaption);
            figCaption.appendTo(figure);
            figure.appendTo(songInfo);
            songInfo.appendTo(songsContainer);
        }

        songsContainer.css('display', '');
    }

    function ajaxError() {
        alert('Error while loading of songs');
    }
});