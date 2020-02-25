//== Class definition

var DatatableDataLocalDemo = function () {
	//== Private functions

	// demo initializer
	var demo = function () {

		var datatable = $('.m_datatable').mDatatable({
			// datasource definition
			data: {
				type: 'local',
				source: dataJSONArray,
			},

			// layout definition
			layout: {
				theme: 'default', // datatable theme
				class: '', // custom wrapper class
				scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
				// height: 450, // datatable's body's fixed height
				footer: false // display/hide footer
			},

			// column sorting
			sortable: false,

			pagination: false,

			search: {
				input: $('#generalSearch')
			},

			// inline and bactch editing(cooming soon)
			// editable: false,

			// columns definition
			columns: [{
				field: "grade",
				title: "Khối",
				width: 50
			}, {
				field: "className",
				title: "Tên lớp",
				sortable: true,
				width: 100
			}, {
				field: "totalStudent",
				title: "Sĩ số",
				width: 50
			}, {
				field: "homeroomTeacher",
				title: "Giáo viên chủ nhiệm"
			}, {
				field: "Actions",
				title: "",
				overflow: 'visible',
				width: 100,
				textAlign: 'right',
				template: function (row) {
					return '\
						<a href="#" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="Edit ">\
                            <i class="la la-edit"></i>\
						</a>\
						<a href="#" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="Delete ">\
                            <i class="la la-trash"></i>\
                        </a>\
					';
				}
			}]
		});

		var query = datatable.getDataSourceQuery();

		$('#m_form_grade').on('change', function () {
			datatable.search($(this).val(), 'grade');
		}).val(typeof query.Status !== 'undefined' ? query.Status : '');

		$('#m_form_grade').selectpicker();

	};

	return {
		//== Public functions
		init: function () {
			// init demo
			demo();
		}
	};
}();

jQuery(document).ready(function () {
	DatatableDataLocalDemo.init();
});