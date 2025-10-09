declare namespace API {
  type AdjustItem = {
    /** 调整原因 */
    adjustReason?: string
    /** 调整类型（1-替换食物，2-调整分量，3-添加食物，4-删除食物） */
    adjustType?: number
    /** 餐次类型（1-早餐，2-午餐，3-晚餐，4-加餐） */
    mealType?: number
    /** 新食物分量（g） */
    newAmount?: number
    /** 新食物ID */
    newFoodId?: number
    /** 原食物分量（g） */
    originalAmount?: number
    /** 原食物ID */
    originalFoodId?: number
  }

  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseDietPlanVO_ = {
    code?: number
    data?: DietPlanVO
    message?: string
  }

  type BaseResponseDietRecordVO_ = {
    code?: number
    data?: DietRecordVO
    message?: string
  }

  type BaseResponseFoodNutritionVO_ = {
    code?: number
    data?: FoodNutritionVO
    message?: string
  }

  type BaseResponseHealthProfileVO_ = {
    code?: number
    data?: HealthProfileVO
    message?: string
  }

  type BaseResponseIPageDietRecordVO_ = {
    code?: number
    data?: IPageDietRecordVO_
    message?: string
  }

  type BaseResponseListDietPlanVO_ = {
    code?: number
    data?: DietPlanVO[]
    message?: string
  }

  type BaseResponseListDietRecordStatisticsVO_ = {
    code?: number
    data?: DietRecordStatisticsVO[]
    message?: string
  }

  type BaseResponseListDietRecordVO_ = {
    code?: number
    data?: DietRecordVO[]
    message?: string
  }

  type BaseResponseListMapStringObject_ = {
    code?: number
    data?: MapStringObject_[]
    message?: string
  }

  type BaseResponseListNutritionAssessmentResponse_ = {
    code?: number
    data?: NutritionAssessmentResponse[]
    message?: string
  }

  type BaseResponseLoginResponse_ = {
    code?: number
    data?: LoginResponse
    message?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseMapStringObject_ = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponseNutritionAssessmentResponse_ = {
    code?: number
    data?: NutritionAssessmentResponse
    message?: string
  }

  type BaseResponsePageFoodNutritionVO_ = {
    code?: number
    data?: PageFoodNutritionVO_
    message?: string
  }

  type BaseResponsePageUserVO_ = {
    code?: number
    data?: PageUserVO_
    message?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUserVO_ = {
    code?: number
    data?: UserVO
    message?: string
  }

  type calculateNutritionUsingPOST1Params = {
    /** 方案ID */
    planId: number
  }

  type confirmDietPlanUsingPOST1Params = {
    /** 方案ID */
    planId: number
  }

  type copyDietPlanUsingPOST1Params = {
    /** 方案ID */
    planId: number
    /** 目标日期 */
    targetDate: string
  }

  type deleteDietPlanUsingDELETE1Params = {
    /** 方案ID */
    planId: number
  }

  type deleteDietRecordUsingDELETE1Params = {
    /** 记录ID */
    recordId: number
  }

  type deleteHealthProfileByUserIdUsingPOST1Params = {
    /** userId */
    userId: number
  }

  type deleteNutritionRequirementUsingGET1Params = {
    /** id */
    id: number
  }

  type DeleteRequest = {
    id?: number
  }

  type DietPlanAdjustRequest = {
    /** 批量调整项目 */
    adjustItems?: AdjustItem[]
    /** 调整原因 */
    adjustReason?: string
    /** 调整类型（1-替换食物，2-调整分量，3-添加食物，4-删除食物） */
    adjustType: number
    /** 是否自动保存调整结果 */
    autoSave?: boolean
    /** 餐次类型（1-早餐，2-午餐，3-晚餐，4-加餐） */
    mealType: number
    /** 新食物分量（g） */
    newAmount?: number
    /** 新食物ID（替换或添加时需要） */
    newFoodId?: number
    /** 原食物分量（g） */
    originalAmount?: number
    /** 原食物ID（替换或删除时需要） */
    originalFoodId?: number
    /** 方案ID */
    planId: number
    /** 是否重新计算营养成分 */
    recalculateNutrition?: boolean
  }

  type DietPlanGenerateRequest = {
    /** 是否自动保存方案 */
    autoSave?: boolean
    /** 烹饪方式偏好 */
    cookingMethods?: string[]
    /** 饮食偏好（素食、无乳糖等） */
    dietaryPreferences?: string[]
    /** 忌口食物 */
    forbiddenFoods?: string[]
    /** 是否包含加餐 */
    includeSnacks?: boolean
    /** 餐次分配比例（早餐:午餐:晚餐:加餐） */
    mealRatio?: string
    /** 方案日期 */
    planDate: string
    /** 方案名称 */
    planName?: string
    /** 营养需求ID（基于营养评估结果） */
    requirementId: number
    /** 食物种类偏好（1-清淡，2-适中，3-重口味） */
    tastePreference?: number
    /** 用户ID */
    userId: number
  }

  type DietPlanVO = {
    /** 早餐内容 */
    breakfast?: MealItem[]
    /** 创建时间 */
    createTime?: string
    /** 晚餐内容 */
    dinner?: MealItem[]
    /** 午餐内容 */
    lunch?: MealItem[]
    nutritionCompliance?: NutritionComplianceVO
    /** 方案日期 */
    planDate?: string
    /** 方案ID */
    planId?: number
    /** 方案名称 */
    planName?: string
    /** 加餐内容 */
    snacks?: MealItem[]
    /** 方案状态（1-草稿，2-已确认，3-已执行） */
    status?: number
    /** 总热量（kcal） */
    totalCalories?: number
    /** 总碳水化合物（g） */
    totalCarbohydrate?: number
    /** 总脂肪（g） */
    totalFat?: number
    /** 总膳食纤维（g） */
    totalFiber?: number
    /** 总蛋白质（g） */
    totalProtein?: number
    /** 更新时间 */
    updateTime?: string
    /** 用户ID */
    userId?: number
  }

  type DietRecordAddRequest = {
    /** 记录时间 */
    createTime: string
    /** 食物分量（g） */
    foodAmount: number
    /** 食物名称 */
    foodName: string
    /** 餐别：1-早餐，2-午餐，3-晚餐，4-加餐 */
    mealType: number
  }

  type DietRecordStatisticsVO = {
    /** 早餐热量（kcal） */
    breakfastCalories?: number
    /** 晚餐热量（kcal） */
    dinnerCalories?: number
    /** 午餐热量（kcal） */
    lunchCalories?: number
    /** 各餐别记录详情 */
    mealStatistics?: MealStatistics[]
    /** 记录条数 */
    recordCount?: number
    /** 加餐热量（kcal） */
    snackCalories?: number
    /** 统计日期 */
    statisticsDate?: string
    /** 总热量（kcal） */
    totalCalories?: number
    /** 总碳水化合物（g） */
    totalCarbohydrate?: number
    /** 总脂肪（g） */
    totalFat?: number
    /** 总蛋白质（g） */
    totalProtein?: number
  }

  type DietRecordUpdateRequest = {
    /** 热量（kcal） */
    calories?: number
    /** 碳水化合物（g） */
    carbohydrate?: number
    /** 脂肪（g） */
    fat?: number
    /** 食物分量（g） */
    foodAmount?: number
    /** 食物名称 */
    foodName?: string
    /** 餐别：1-早餐，2-午餐，3-晚餐，4-加餐 */
    mealType?: number
    /** 蛋白质（g） */
    protein?: number
    /** 记录日期 */
    recordDate?: string
    /** 记录ID */
    recordId: number
  }

  type DietRecordVO = {
    /** 热量（kcal） */
    calories?: number
    /** 碳水化合物（g） */
    carbohydrate?: number
    /** 创建时间 */
    createTime?: string
    /** 脂肪（g） */
    fat?: number
    /** 食物分量（g） */
    foodAmount?: number
    /** 食物名称 */
    foodName?: string
    /** 餐别：1-早餐，2-午餐，3-晚餐，4-加餐 */
    mealType?: number
    /** 餐别名称 */
    mealTypeName?: string
    /** 蛋白质（g） */
    protein?: number
    /** 记录日期 */
    recordDate?: string
    /** 记录ID */
    recordId?: number
    /** 更新时间 */
    updateTime?: string
    /** 用户ID */
    userId?: number
  }

  type FoodNutritionAddRequest = {
    /** 每100g钙（mg） */
    calciumPer100g?: number
    /** 每100g热量（kcal） */
    caloriesPer100g: number
    /** 每100g碳水化合物（g） */
    carbohydratePer100g: number
    /** 每100g脂肪（g） */
    fatPer100g: number
    /** 每100g膳食纤维（g） */
    fiberPer100g?: number
    /** 食物分类 */
    foodCategory?: string
    /** 食物名称 */
    foodName: string
    /** 每100g铁（mg） */
    ironPer100g?: number
    /** 每100g蛋白质（g） */
    proteinPer100g: number
    /** 每100g维生素A（μg） */
    vitaminAPer100g?: number
    /** 每100g维生素C（mg） */
    vitaminCPer100g?: number
  }

  type FoodNutritionUpdateRequest = {
    /** 每100g钙（mg） */
    calciumPer100g?: number
    /** 每100g热量（kcal） */
    caloriesPer100g?: number
    /** 每100g碳水化合物（g） */
    carbohydratePer100g?: number
    /** 每100g脂肪（g） */
    fatPer100g?: number
    /** 每100g膳食纤维（g） */
    fiberPer100g?: number
    /** 食物分类 */
    foodCategory?: string
    /** 食物ID */
    foodId: number
    /** 食物名称 */
    foodName?: string
    /** 每100g铁（mg） */
    ironPer100g?: number
    /** 每100g蛋白质（g） */
    proteinPer100g?: number
    /** 每100g维生素A（μg） */
    vitaminAPer100g?: number
    /** 每100g维生素C（mg） */
    vitaminCPer100g?: number
  }

  type FoodNutritionVO = {
    /** 每100g钙（mg） */
    calciumPer100g?: number
    /** 每100g热量（kcal） */
    caloriesPer100g?: number
    /** 每100g碳水化合物（g） */
    carbohydratePer100g?: number
    /** 创建时间 */
    createTime?: string
    /** 每100g脂肪（g） */
    fatPer100g?: number
    /** 每100g膳食纤维（g） */
    fiberPer100g?: number
    /** 食物分类 */
    foodCategory?: string
    /** 食物ID */
    foodId?: number
    /** 食物名称 */
    foodName?: string
    /** 每100g铁（mg） */
    ironPer100g?: number
    /** 每100g蛋白质（g） */
    proteinPer100g?: number
    /** 更新时间 */
    updateTime?: string
    /** 每100g维生素A（μg） */
    vitaminAPer100g?: number
    /** 每100g维生素C（mg） */
    vitaminCPer100g?: number
  }

  type getAllDietPlansUsingGET1Params = {
    /** 结束日期 */
    endDate?: string
    /** 开始日期 */
    startDate?: string
    /** 用户ID */
    userId?: number
  }

  type getDietPlanByUserAndDateUsingGET1Params = {
    /** 方案日期 */
    date: string
    /** 用户ID */
    userId: number
  }

  type getDietPlansByUserUsingGET1Params = {
    /** 结束日期 */
    endDate?: string
    /** 开始日期 */
    startDate?: string
    /** 用户ID */
    userId: number
  }

  type getDietRecordByIdUsingGET1Params = {
    /** 记录ID */
    recordId: number
  }

  type getDietRecordsByDateUsingGET1Params = {
    /** 记录日期 */
    recordDate: string
  }

  type getDietRecordStatisticsUsingGET1Params = {
    /** 结束日期 */
    endDate: string
    /** 开始日期 */
    startDate: string
  }

  type getFoodNutritionByIdUsingGET1Params = {
    /** id */
    id: number
  }

  type getHealthProfileByUserIdUsingGET1Params = {
    /** userId */
    userId: number
  }

  type getMyDietPlansUsingGET1Params = {
    /** 结束日期 */
    endDate?: string
    /** 开始日期 */
    startDate?: string
  }

  type getNutritionRequirementByIdUsingGET1Params = {
    /** id */
    id: number
  }

  type getUserByIdUsingGET1Params = {
    /** id */
    id?: number
  }

  type HealthProfileCreateRequest = {
    activityLevel?: number
    allergies?: string
    chronicDiseases?: string
    dietaryPreferences?: string
    exerciseHabits?: string
    forbiddenFoods?: string
  }

  type HealthProfileUpdateRequest = {
    activityLevel?: number
    allergies?: string
    chronicDiseases?: string
    dietaryPreferences?: string
    exerciseHabits?: string
    forbiddenFoods?: string
    profileId?: number
  }

  type HealthProfileVO = {
    activityLevel?: number
    activityLevelDesc?: string
    allergies?: string
    chronicDiseases?: string
    createTime?: string
    dietaryPreferences?: string
    exerciseHabits?: string
    forbiddenFoods?: string
    profileId?: number
    updateTime?: string
    userId?: number
  }

  type IPageDietRecordVO_ = {
    current?: number
    pages?: number
    records?: DietRecordVO[]
    size?: number
    total?: number
  }

  type listFoodNutritionByPageGetUsingGET1Params = {
    /** 食物分类 */
    foodCategory?: string
    /** 食物名称（模糊查询） */
    foodName?: string
    /** 最大热量（kcal/100g） */
    maxCalories?: number
    /** 最大碳水化合物含量（g/100g） */
    maxCarbohydrate?: number
    /** 最大脂肪含量（g/100g） */
    maxFat?: number
    /** 最大蛋白质含量（g/100g） */
    maxProtein?: number
    /** 最小热量（kcal/100g） */
    minCalories?: number
    /** 最小碳水化合物含量（g/100g） */
    minCarbohydrate?: number
    /** 最小脂肪含量（g/100g） */
    minFat?: number
    /** 最小蛋白质含量（g/100g） */
    minProtein?: number
    /** 排序字段 */
    sortField?: string
    /** 排序方向（asc/desc） */
    sortOrder?: string
    current?: number
    pageSize?: number
  }

  type LoginRequest = {
    /** 密码 */
    password: string
    /** 用户名 */
    username: string
  }

  type LoginResponse = {
    /** 邮箱 */
    email?: string
    /** Token过期时间（毫秒） */
    expiresIn?: number
    /** 手机号 */
    phone?: string
    /** JWT Token */
    token?: string
    /** Token类型 */
    tokenType?: string
    /** 用户ID */
    userId?: number
    /** 用户角色 */
    userRole?: string
    /** 用户名 */
    username?: string
  }

  type MapStringObject_ = true

  type MapStringObject_1 = true

  type MealItem = {
    /** 食物分量（g） */
    amount?: number
    /** 热量（kcal） */
    calories?: number
    /** 碳水化合物（g） */
    carbohydrate?: number
    /** 烹饪方式 */
    cookingMethod?: string
    /** 脂肪（g） */
    fat?: number
    /** 膳食纤维（g） */
    fiber?: number
    /** 食物分类 */
    foodCategory?: string
    /** 食物ID */
    foodId?: number
    /** 食物名称 */
    foodName?: string
    /** 蛋白质（g） */
    protein?: number
    /** 备注 */
    remark?: string
  }

  type MealStatistics = {
    /** 热量（kcal） */
    calories?: number
    /** 碳水化合物（g） */
    carbohydrate?: number
    /** 脂肪（g） */
    fat?: number
    /** 餐别：1-早餐，2-午餐，3-晚餐，4-加餐 */
    mealType?: number
    /** 餐别名称 */
    mealTypeName?: string
    /** 蛋白质（g） */
    protein?: number
    /** 记录条数 */
    recordCount?: number
  }

  type NutritionAssessmentRequest = {
    /** 活动水平（1-久坐，2-轻度，3-中度，4-重度） */
    activityLevel: number
    /** 年龄 */
    age: number
    /** 性别（0-女，1-男） */
    gender: number
    /** 健康目标（1-减肥，2-增肌，3-维持健康） */
    healthGoal: number
    /** 身高（cm） */
    height: number
    /** 是否保存评估结果 */
    saveResult?: boolean
    /** 体重（kg） */
    weight: number
  }

  type NutritionAssessmentResponse = {
    /** 评估日期 */
    assessmentDate?: string
    /** BMI值 */
    bmi?: number
    /** BMI状态（偏瘦/正常/超重/肥胖） */
    bmiStatus?: string
    /** 基础代谢率（BMR） */
    bmr?: number
    /** 钙需求（mg） */
    calciumRequirement?: number
    /** 碳水化合物需求（g） */
    carbohydrateRequirement?: number
    /** 创建时间 */
    createTime?: string
    /** 每日热量需求（kcal） */
    dailyCalories?: number
    /** 脂肪需求（g） */
    fatRequirement?: number
    /** 纤维需求（g） */
    fiberRequirement?: number
    /** 健康建议 */
    healthAdvice?: string
    /** 铁需求（mg） */
    ironRequirement?: number
    /** 蛋白质需求（g） */
    proteinRequirement?: number
    /** 需求ID */
    requirementId?: number
    /** 用户ID */
    userId?: number
    /** 维生素A需求（μg） */
    vitaminARequirement?: number
    /** 维生素C需求（mg） */
    vitaminCRequirement?: number
  }

  type NutritionComplianceVO = {
    /** 热量达标率（%） */
    caloriesComplianceRate?: number
    /** 碳水化合物达标率（%） */
    carbohydrateComplianceRate?: number
    /** 脂肪达标率（%） */
    fatComplianceRate?: number
    /** 膳食纤维达标率（%） */
    fiberComplianceRate?: number
    /** 营养建议 */
    nutritionAdvice?: string[]
    /** 整体营养评分（0-100） */
    overallScore?: number
    /** 蛋白质达标率（%） */
    proteinComplianceRate?: number
  }

  type NutritionQueryRequest = {
    current?: number
    /** 评估结束日期 */
    endDate?: string
    /** 最大热量需求 */
    maxCalories?: number
    /** 最小热量需求 */
    minCalories?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    /** 评估开始日期 */
    startDate?: string
    /** 用户ID */
    userId?: number
  }

  type OrderItem = {
    asc?: boolean
    column?: string
  }

  type PageFoodNutritionVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: FoodNutritionVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageUserVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: UserVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type queryDietRecordsGetUsingGET1Params = {
    /** 结束日期 */
    endDate?: string
    /** 食物名称（模糊查询） */
    foodName?: string
    /** 餐别：1-早餐，2-午餐，3-晚餐，4-加餐 */
    mealType?: number
    /** 开始日期 */
    startDate?: string
    /** 用户ID */
    userId?: number
    current?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
  }

  type RegisterRequest = {
    /** 确认密码 */
    confirmPassword: string
    /** 邮箱 */
    email?: string
    /** 密码 */
    password: string
    /** 手机号 */
    phone?: string
    /** 用户名 */
    username: string
  }

  type UserAddRequest = {
    age?: number
    email?: string
    gender?: number
    healthGoal?: number
    height?: number
    password?: string
    phone?: string
    realName?: string
    userRole?: string
    username?: string
    weight?: number
  }

  type UserQueryRequest = {
    current?: number
    email?: string
    gender?: number
    healthGoal?: number
    maxAge?: number
    minAge?: number
    pageSize?: number
    phone?: string
    realName?: string
    sortField?: string
    sortOrder?: string
    status?: number
    userId?: number
    userRole?: string
    username?: string
  }

  type UserUpdateRequest = {
    age?: number
    email?: string
    gender?: number
    healthGoal?: number
    height?: number
    phone?: string
    realName?: string
    status?: number
    userId?: number
    userRole?: string
    username?: string
    weight?: number
  }

  type UserVO = {
    age?: number
    createTime?: string
    email?: string
    gender?: number
    genderDesc?: string
    healthGoal?: number
    healthGoalDesc?: string
    height?: number
    phone?: string
    realName?: string
    status?: number
    statusDesc?: string
    updateTime?: string
    userId?: number
    userRole?: string
    username?: string
    weight?: number
  }
}
