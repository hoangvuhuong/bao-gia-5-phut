//== Class definition

var tableOptions = {
		// datasource definition
		data: {
			type: 'local',
			source: studentJSONArray,
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
			field:        'select',
            title:      '<label class="m-checkbox m-checkbox--single m-checkbox--solid m-checkbox--brand"><input type="checkbox" value="" class="m-group-checkable" id="m_group_checkable"><span></span></label>',
            template: function (row) {
				return '<label class="m-checkbox m-checkbox--single m-checkbox--solid m-checkbox--brand"><input type="checkbox" value="" class="m-checkable"><span></span></label>';
			}
        },{
			field: "position",
			title: "STT",
			width: 50
		}, {
			field: "studentId",
			title: "Mã học sinh",
			width: 100
		}, {
			field: "studentName",
			title: "Họ tên",
			width: 150
		}, {
			field: "pickupPointId",
			title: "Điểm đón"
		}, {
			field: "dropPointId",
			title: "Điểm trả"
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
	};
var datatable = $('.m_datatable').mDatatable(tableOptions);

var query = datatable.getDataSourceQuery();

var getStudentsOfClass = function(classId) {
	var urlStudents = `students/class/${classId}`;
    /* Prepare ajax get settings */
    var settings = {
    	type: 'GET',
        url: urlStudents,
    }

    /* Send request to backend with above settings and provide success callback */
    $.ajax(settings)
    	.done(function(result) {

            /* 
                Replace your fragment with newly rendered fragment. 
                Reference fragment by provided id
            */
    		studentJSONArray = JSON.parse(result);
    		datatable.destroy();
    		tableOptions.data.source = studentJSONArray;
    		datatable = $('.m_datatable').mDatatable(tableOptions);
    	})
    	.fail(function(result) {
    		//
    	});
};

jQuery(document).ready(function () {
	$('#m_form_class').on('change', function () {
		getStudentsOfClass($(this).val());
	}).val(typeof query.Status !== 'undefined' ? query.Status : '');

	$('#m_form_class').selectpicker();
	$('#checkbox_form').on('change', 'input[type=checkbox]', function() {
		alert('asdsadasd');
	});
});