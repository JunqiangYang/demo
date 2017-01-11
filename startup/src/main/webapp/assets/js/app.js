(function ($) {
  'use strict';

  $(function () {
    var $fullText = $('.admin-fullText');
    $('#admin-fullscreen').on('click', function () {
      $.AMUI.fullscreen.toggle();
    });

    $(document).on($.AMUI.fullscreen.raw.fullscreenchange, function () {
      $fullText.text($.AMUI.fullscreen.isFullscreen ? '退出全屏' : '开启全屏');
    });

    $(".am-pagination li a").each(function () {
      var href = $(this).attr("href");
      if (href != "#") {
        $(this).attr("href", href + "&" + $("form.search").serialize());
      }
    });

    $('#delete-confirm').on('closed.modal.amui', function () {
      $(this).removeData('amui.modal');
    });

    $(".delete-model").on("click", function () {
      var delLink = this;
      $('#delete-confirm').modal({
        onConfirm: function (options) {
          var param;
          var href = window.location.href.substring("?");
          if (href.indexOf("?") > -1) {
            param = href.substring(href.indexOf("?"));
          }
          window.location.href = $(delLink).attr("data-href") + param;
        },
        onCancel: function () {
        }
      });
    });
  });
})(jQuery);
