# PicassoCache
A memory cache for Picasso. We can customize memory cache by this.

# Use

    // Loading pic.
    PicassoCache.getPicasso()
            .load("http://www.google.com/images/srpr/logo11w.png")
            .into(imageView);

    // Clear memory cache.
    PicassoCache.clearMemCache();

    // shutdown Picasso.
    PicassoCache.shutdown();
