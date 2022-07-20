package com.flab.yousinsa.admin.controller.api;

import static com.flab.yousinsa.ApiDocumentUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.yousinsa.admin.domain.dtos.AdminStoreDto;
import com.flab.yousinsa.admin.service.contract.AdminStoreService;
import com.flab.yousinsa.admin.service.exceptions.NotPermittedException;
import com.flab.yousinsa.annotation.UnitTest;
import com.flab.yousinsa.store.enums.StoreStatus;
import com.flab.yousinsa.user.controller.aop.AuthenticateAspect;
import com.flab.yousinsa.user.domain.dtos.AuthUser;
import com.flab.yousinsa.user.domain.enums.UserRole;

@WebMvcTest(AdminStoreController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureRestDocs
class AdminStoreControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	AdminStoreService adminStoreService;

	AuthUser admin;
	AuthUser notAdmin;

	AdminStoreDto firstStoreDto;
	AdminStoreDto secondStoreDto;
	AdminStoreDto thirdStoreDto;

	@BeforeEach
	public void setUp() {
		admin = new AuthUser(1L, "admin", "admin@yousinsa.com", UserRole.ADMIN);
		notAdmin = new AuthUser(2L, "notAdmin", "notAdmin@yousinsa.com", UserRole.STORE_OWNER);

		firstStoreDto = new AdminStoreDto(1L, "firstStore", 1L, StoreStatus.ACCEPTED);
		secondStoreDto = new AdminStoreDto(2L, "secondStore", 2L, StoreStatus.REJECTED);
		thirdStoreDto = new AdminStoreDto(3L, "thirdStore", 3L, StoreStatus.REJECTED);
	}

	@UnitTest
	@Test
	@DisplayName("모든 Store 조회 API Doc")
	public void getAllRegisteredStorePage() throws Exception {
		// given
		Pageable pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
		List<AdminStoreDto> storeDtoList = List.of(firstStoreDto, secondStoreDto, thirdStoreDto);
		Page<AdminStoreDto> storeDtoPage = new PageImpl<>(storeDtoList, pageRequest, storeDtoList.size());

		MockHttpSession session = new MockHttpSession();
		session.setAttribute(AuthenticateAspect.AUTH_USER, admin);

		given(adminStoreService.getAllStores(pageRequest)).willReturn(storeDtoPage);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/admin/v1/stores?page=0&size=3&sort=id,desc").session(session)
		);

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(storeDtoPage)))
			.andDo(print())
			.andDo(
				document("admin-store-list",
					getDocumentRequest(),
					getDocumentResponse(),
					requestParameters(
						parameterWithName("page").description("갖고 오고자 하는 Page 번호"),
						parameterWithName("size").description("페이지당 갖고 올 Item 갯수"),
						parameterWithName("sort").description("정렬할 필드와 정렬 순서(id, asc/desc)")
					),
					responseFields(
						fieldWithPath("content").type(JsonFieldType.ARRAY).description("등록된 Store 리스트"),
						fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
						fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
						fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
						fieldWithPath("content[].storeId").type(JsonFieldType.NUMBER).description("스토어 ID(PK)"),
						fieldWithPath("content[].storeName").type(JsonFieldType.STRING).description("스토어 이름"),
						fieldWithPath("content[].ownerId").type(JsonFieldType.NUMBER).description("Store 관리자 ID(PK)"),
						fieldWithPath("content[].storeStatus").type(JsonFieldType.STRING).description("Store 현재 상태"),
						fieldWithPath("pageable.sort.unsorted").ignored(),
						fieldWithPath("pageable.sort.sorted").ignored(),
						fieldWithPath("pageable.sort.empty").ignored(),
						fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("요청한 Page 번호"),
						fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("요청한 Page 크기"),
						fieldWithPath("pageable.offset").ignored(),
						fieldWithPath("pageable.paged").ignored(),
						fieldWithPath("pageable.unpaged").ignored(),
						fieldWithPath("last").ignored(),
						fieldWithPath("numberOfElements").ignored(),
						fieldWithPath("size").ignored(),
						fieldWithPath("number").ignored(),
						fieldWithPath("first").ignored(),
						fieldWithPath("sort").ignored(),
						fieldWithPath("sort.unsorted").ignored(),
						fieldWithPath("sort.sorted").ignored(),
						fieldWithPath("sort.empty").ignored(),
						fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는 페이지 여부")
					)
				)
			);
	}

	@UnitTest
	@Test
	@DisplayName("등록되어 있는 모든 Store 조회시 Admin이 아닌 경우")
	public void getAllRegisteredStorePageByNotAdmin() throws Exception {
		// given
		Pageable pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
		List<AdminStoreDto> storeDtoList = List.of(firstStoreDto, secondStoreDto, thirdStoreDto);
		Page<AdminStoreDto> storeDtoPage = new PageImpl<>(storeDtoList, pageRequest, storeDtoList.size());

		MockHttpSession session = new MockHttpSession();
		session.setAttribute(AuthenticateAspect.AUTH_USER, notAdmin);

		given(adminStoreService.getAllStores(pageRequest)).willReturn(storeDtoPage);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/admin/v1/stores?page=0&size=3&sort=id,desc").session(session)
		);

		// then
		resultActions.andExpect(status().isForbidden())
			.andExpect((result) ->
				assertThat(result.getResolvedException())
					.isInstanceOf(NotPermittedException.class)
					.hasMessageContaining("store list can be viewed by admin"));
	}
}
