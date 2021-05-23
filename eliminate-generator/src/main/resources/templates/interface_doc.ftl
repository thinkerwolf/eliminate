<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>${serverName}服文档-${moduleName}</title>
    <meta charset="UTF-8">
    <meta name="keywords" content="doc"/>
    <meta name="description" content="文档文字版"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimal-ui, shrink-to-fit=no">
    <link rel="stylesheet" href="../css/github-markdown.css">
    <style>
    	.markdown-body {
    		box-sizing: border-box;
    		min-width: 200px;
    		max-width: 980px;
    		margin: 0 auto;
    		padding: 45px;
    	}

    	@media (max-width: 767px) {
    		.markdown-body {
    			padding: 15px;
    		}
    	}
    </style>
</head>

<body>
    <article class="markdown-body">
     ${body}
    </article>
</body>

</html>