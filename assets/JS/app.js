var App = function () {

    var isIE8 = false; // IE8 mode
    var isIE9 = false;
    var currentPage = "index"; // current page

    var setEqualHeight = function (columns) {
        var tallestColumn = 0;
        columns = jQuery(columns);
        columns.each(function () {
            var currentHeight = $(this).height();
            if (currentHeight > tallestColumn) {
                tallestColumn = currentHeight;
            }
        });
        columns.height(tallestColumn);
    }


    var handleMainMenu = function () {
        jQuery('.page-sidebar .has-sub > a').click(function () {

            var handleContentHeight = function () {
                var content = $('.page-content');
                var sidebar = $('.page-sidebar');

                if (!content.attr("data-height")) {
                    content.attr("data-height", content.height());
                }


                if (sidebar.height() > content.height()) {
                    content.css("min-height", sidebar.height() + 20);
                } else {
                    content.css("min-height", content.attr("data-height"));
                }
            }

            var last = jQuery('.has-sub.open', $('.page-sidebar'));
            if (last.size() == 0) {
                //last = jQuery('.has-sub.active', $('.page-sidebar'));
            }
            last.removeClass("open");
            jQuery('.arrow', last).removeClass("open");
            jQuery('.sub', last).slideUp(200);

            var sub = jQuery(this).next();
            if (sub.is(":visible")) {
                jQuery('.arrow', jQuery(this)).removeClass("open");
                jQuery(this).parent().removeClass("open");
                sub.slideUp(200, function () {
                    handleContentHeight();
                });
            } else {
                jQuery('.arrow', jQuery(this)).addClass("open");
                jQuery(this).parent().addClass("open");
                sub.slideDown(200, function () {
                    handleContentHeight();
                });
            }
        });
    }

    var handleSidebarToggler = function () {

        var container = $(".page-container");

        $('.page-sidebar .sidebar-toggler').click(function () {
            $(".sidebar-search").removeClass("open");
            var container = $(".page-container");
            if (container.hasClass("sidebar-closed") === true) {
                container.removeClass("sidebar-closed");
            } else {
                container.addClass("sidebar-closed");
            }

        });

        // handle the search bar close
        $('.sidebar-search .remove').click(function () {
            $('.sidebar-search').removeClass("open");
        });

        // handle the search query submit on enter press
        $('.sidebar-search input').keypress(function (e) {
            if (e.which == 13) {
                window.location.href = "extra_search.html";
                return false; //<---- Add this line
            }
        });

        // handle the search submit
        $('.sidebar-search .submit').click(function () {
            if ($('.page-container').hasClass("sidebar-closed")) {
                if ($('.sidebar-search').hasClass('open') == false) {
                    $('.sidebar-search').addClass("open");
                } else {
                    window.location.href = "extra_search.html";
                }
            } else {
                window.location.href = "extra_search.html";
            }
        });
    }

       return {
        init: function () {       
            handleMainMenu(); 
            handleSidebarToggler();
        }

     };

}();