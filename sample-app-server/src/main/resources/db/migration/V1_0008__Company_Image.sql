-- these are just to upload some images to the database , you can put any valid image URL if these don't exist anymore.
update company set image = FILE_READ('http://www.logoopenstock.com/media/users/379/778/raw/65edc5b9fd755017e29be413f20bb444-logo-logo-design-download-free-psd-file.jpg')
where id=1;

update company set image = FILE_READ('http://www.logoopenstock.com/media/users/359/784/raw/85bf9356937981e5e7c93dd4e50f9caf-custom-business-logo-template-4.jpg')
where id=2;


