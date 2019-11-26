// 共通化
function includeHtml(url) {
	$.ajax({
		url : url, // 読み込むHTMLファイル
		cache : false,
		async : false,
		dataType : 'html',
		success : function(html) {
			document.write(html);
		}
	});
}

// サブミット用関数
function submitAction(url, name, i) {

	if(name){
		document.getElementsByClassName(name)[i].action = url;
		document.getElementsByClassName(name)[i].submit();
	}else{
		$('form').attr('action', url);
		$('form').submit();
	}
}

// 検索機能
$(function() {
	searchWord = function() {
		var searchText = $(this).val(), // 検索ボックスに入力された値
		targetText;
		hitNum = '';
		count = 0;

		// 検索結果エリアの表示を空にする
		$('.search-result__hit-num').empty();

		$('.target-area')
				.each(
						function() {
							targetText = $(this)[0]
									.getElementsByClassName('name')[0].innerText;

							// 検索対象となるリストに入力された文字列が存在するかどうかを判断
							if (targetText.indexOf(searchText) != -1) {
								$(this).removeClass('hidden');
								count = count + 1;
							} else {
								$(this).addClass('hidden');
							}
						});
		// ヒットの件数をページに出力
		hitNum = '<span>検索結果</span>：' + count + '件見つかりました。';
		$('.search-result__hit-num').append(hitNum);
	};

	// searchWordの実行
	$('#search-text').on('input', searchWord);
});

//プレビュー機能
$(function() {
	$('#imageUpload').change(function(e) {
		//ファイルオブジェクトを取得する
		var file = e.target.files[0];
		var reader = new FileReader();

		//画像でない場合は処理終了
		if (file.type.indexOf('image') < 0) {
			alert('画像ファイルを指定してください。');
			document.getElementById('imageUpload').value = '';
			return false;
		}

		//アップロードした画像を設定する
		reader.onload = (function(file) {
			return function(e) {
				$('#imageFile').attr('src', e.target.result);
				$('#imageFile').attr('title', file.name);
			};
		})(file);

		reader.readAsDataURL(file);

	});
});

// チェック機能
function select_checked(id, value) {
	select = document.getElementById(id).options;
	for (let i = 0; i < select.length; i++) {
		if (select[i].value === value) {
			select[i].selected = true;
			break;
		}
	}
}

// ツールチップ
$(function() {
	$('[data-toggle="tooltip"]').tooltip()
});
