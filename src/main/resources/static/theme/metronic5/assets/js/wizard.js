var WizardDemo = function() {
	$("#m_wizard");
	var e, r, i = $("#m_form");
	return {
		init : function() {
			var n;
			$("#m_wizard"), i = $("#m_form"), (r = new mWizard("m_wizard", {
				startStep : ($("#quotationId").val() == null || $("#quotationId").val() == "") ? 1 : 2
			})).on("beforeNext", function(r) {
				!0 !== e.form() && r.stop()
			}), r.on("change", function(e) {
				if (2 === e.getStep()) {
					$("#btnSaveSubmit").removeClass("d-none")
					$("#btnReset").removeClass("d-none")
				} else {
					$("#btnSaveSubmit").addClass("d-none")
					$("#btnReset").addClass("d-none")
				}
				mUtil.scrollTop()
			}), e = i.validate({
				ignore : ":hidden",
				rules : {
					url : {
						required : !0
					},
					approver : {
						required : !0
					},
					dueDate : {
						required : !0
					},
					partnerId : {
						required : !0
					},
					partner : {
						required : !0
					},
					inChargePartner : {
						required : !0
					},
					googleSheetLink : {
						required : !0
					},
					mainProductService : {
						required : !0
					},
					expectedBudget : {
						required : !0
					},
					geoArea : {
						required : !0
					},
					usedToAdvertise : {
						required : !0
					},
					price : {
						required : !0,
						digits : !0
					},
					display : {
						required : !0
						
					},
					ctr : {
						required : !0,
						number : !0
					},
					price_re : {
						digits : !0
					},
					display_re : {
						digits : !0
					},
					ctr_re : {
						number : !0
					},
					price_gdn : {
						digits : !0
					},
					display_gdn : {
						digits : !0
					},
					ctr_gdn : {
						number : !0
					},
					price_shop : {
						digits : !0
					},
					display_shop : {
						digits : !0
					},
					ctr_shop : {
						number : !0
					}
				},
				invalidHandler : function(e, r) {
					$('#form_error_msg').html(
							'Nhập thông tin báo giá lỗi, vui lòng nhập lại!');
					$("#m_form_1_msg").removeClass("m--hide").show(), mUtil
							.scrollTop();
				},
				submitHandler : function(e) {
//					performPost(e);
					saveQuotation()
				}
			}), (n = i.find('[data-wizard-action="next"]')).on("click",
					function(r) {

					})
		}
	}
}();

jQuery(document).ready(
		function() {
			WizardDemo.init()

			$("#re-marketing-checkbox").change(
					function() {
						var input = [ "#price_re", "#ctr_re", "#target_re",
								"#display_re", "input[name=charged_re]" ]
						if ($(this).is(":checked")) {
							$.each(input, function(i, v) {
								$(v).prop('disabled', false);
								$(v).prop('required', true);
							})
						} else {
							$.each(input, function(i, v) {
								$(v).prop('disabled', true);
								$(v).val(null);
								$(v).prop('required', false);
								$(v).parent().parent().parent().removeClass(
										'has-danger');
							})
						}
					})

			$("#google-GDN-checkbox").change(
					function() {
						var input = [ "#price_gdn", "#ctr_gdn", "#display_gdn",
								"#target_gdn", "input[name=charged_gdn]" ]
						if ($(this).is(":checked")) {
							$.each(input, function(i, v) {
								$(v).prop('disabled', false);
								$(v).prop('required', true);
							})
						} else {
							$.each(input, function(i, v) {
								$(v).prop('disabled', true);
								$(v).val(null);
								$(v).prop('required', false);
								$(v).parent().parent().parent().removeClass(
										'has-danger');
							})
						}
					})

			$("#google-shopping-checkbox").change(
					function() {
						var input = [ "#price_shop", "#ctr_shop",
								"#target_shop", "#display_shop",
								"input[name=charged_shop]" ]
						if ($(this).is(":checked")) {
							$.each(input, function(i, v) {
								$(v).prop('disabled', false);
								$(v).prop('required', true);
							})
						} else {
							$.each(input, function(i, v) {
								$(v).prop('disabled', true);
								$(v).val(null);
								$(v).prop('required', false);
								$(v).parent().parent().parent().removeClass(
										'has-danger');
							})
						}
					})

			$("#price").change(
					function() {
						$("#txt_price").text(
								$("#price").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#price_re").change(
					function() {
						$("#txt_price_re").text(
								$("#price_re").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#price_gdn").change(
					function() {
						$("#txt_price_gdn").text(
								$("#price_gdn").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#price_shop").change(
					function() {
						$("#txt_price_shop").text(
								$("#price_shop").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#display").change(
					function() {
						$("#txt_display").text(
								$("#display").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#display_re").change(
					function() {
						$("#txt_display_re").text(
								$("#display_re").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#display_gdn").change(
					function() {
						$("#txt_display_gdn").text(
								$("#display_gdn").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})

			$("#display_shop").change(
					function() {
						$("#txt_display_shop").text(
								$("#display_shop").val().replace(
										/\B(?=(\d{3})+(?!\d))/g, ","))
					})
		});