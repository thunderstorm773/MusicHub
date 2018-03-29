$('#addComment').on('click', function (e) {
    e.preventDefault();
    let commentBox = $('#comment');
    let commentContent = commentBox.val().trim();
    const commentMinLen = 10;
    if (!isCommentValid()) {
        showInvalidCommentError();
        return;
    }

    postComment();

    function postComment() {
        let csrf = $('#_csrf').val();
        let songId = $('#songId').val();
        let postCommentUrl = '/comments/post';
        $.ajax({
            url: postCommentUrl,
            method: 'POST',
            data: {
                _csrf: csrf,
                commentContent: commentContent,
                songId: songId
            },

            success: renderPostedComment,
            error: error,
        });

        function renderPostedComment(result) {
            if (!result) {
                return;
            }

            commentBox.val('');
            let allCommentsDiv = $('#allComments');
            let comment = composeComment(result);
            comment.prependTo(allCommentsDiv);

            function composeComment(result) {
                let commentJson = JSON.parse(result);
                let authorUsername = commentJson['authorUsername'];
                let status = getStatus(commentJson['status']);
                let publishedOn = formatDate(commentJson['publishedOn']);
                let content = encodeHTML(commentJson['content']);

                let commentDiv = $('<div class="row justify-content-center mt-4">' +
                    '<div class="list-group col-md-6">' +
                    '<a href="#" class="list-group-item list-group-item-action flex-column active"' +
                    ' style="background-color: #282828">' +
                    '<div class="d-flex w-100 justify-content-between">' +
                    `<p class="mb-1"><strong>${authorUsername}</strong></p>` +
                    `<small>${status}</small>` +
                    '</div>' +
                    '<div class="d-flex w-100 justify-content-between">' +
                    `<small>${publishedOn}</small>` +
                    '</div>' +
                    `<p class="mb-1" style="word-break: break-all">${content}</p>` +
                    '</a>' +
                    '</div>' +
                    '</div>');

                return commentDiv;
            }

            function encodeHTML(html) {
                return html
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/"/g, '&quot;');
            }
            
            function formatDate(dateStr) {
                let date = new Date(dateStr);
                let day = date.getDate();
                let month = date.getMonth() + 1;
                let year = date.getFullYear();
                return `${day}/${month}/${year}`;
            }
            
            function getStatus(enumeration) {
                let statusByEnumeration = {
                    'PENDING': 'Waiting for review',
                    'APPROVED': 'Approved',
                    'REJECTED': 'Rejected'
                };

                return statusByEnumeration[enumeration];
            }
        }

        function error() {
            alert('Error while posting a comment');
        }
    }

    function showInvalidCommentError() {
        const errorMsg = `Length of comment must be at least ${commentMinLen} symbols`;
        alert(errorMsg);
    }

    function isCommentValid() {
        return commentContent.length >= commentMinLen;
    }
});