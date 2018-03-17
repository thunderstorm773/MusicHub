$('#file').on('change', function (e) {
    let allFiles = this.files;
    if (allFiles.length === 0) {
        return;
    }

    const bytesInMB = 1048576;
    const maxSongSizeInMB = 10;
    const allowedContentTypes = ['audio/mp3', 'audio/mpeg'];
    const invalidFileFormatMsg = 'File format is not .mp3';
    const exceededMaxSizeMsg = 'File exceeded max allowed size(10MB)';

    let fileErrorsDiv = $('#fileErrors');
    fileErrorsDiv.css('display', 'none');
    fileErrorsDiv.empty();
    enableSubmitButtom();
    let invalidFeedback = $('<div class="invalid-feedback" style="display:block"></div>');
    let songFile = allFiles[0];
    let songContentType = songFile.type;
    let songSizeInMB = songFile.size / bytesInMB;
    let errors = [];
    if (songSizeInMB > maxSongSizeInMB) {
        let error = invalidFeedback.text(exceededMaxSizeMsg);
        errors.push(error);
    }

    if ($.inArray(songContentType, allowedContentTypes) === -1) {
        let error = invalidFeedback.text(invalidFileFormatMsg);
        errors.push(error);
    }

    if (errors.length > 0) {
        for (let error of errors) {
            error.appendTo(fileErrorsDiv);
        }

        disableSubmitButtom();
    }

    fileErrorsDiv.css('display', 'block');
});

function disableSubmitButtom() {
    let uploadSongBtn = $('#uploadSongBtn');
    uploadSongBtn.prop('disabled', true);
}

function enableSubmitButtom() {
    let uploadSongBtn = $('#uploadSongBtn');
    uploadSongBtn.prop('disabled', false);
}