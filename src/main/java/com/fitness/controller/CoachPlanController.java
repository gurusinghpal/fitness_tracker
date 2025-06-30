package com.fitness.controller;

import com.fitness.model.WorkoutPlan;
import com.fitness.model.DietPlan;
import com.fitness.model.User;
import com.fitness.model.Coach;
import com.fitness.service.WorkoutPlanService;
import com.fitness.service.DietPlanService;
import com.fitness.service.UserService;
import com.fitness.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coach")
public class CoachPlanController {
    private final WorkoutPlanService workoutPlanService;
    private final DietPlanService dietPlanService;
    private final UserService userService;
    private final CoachService coachService;

    @Autowired
    public CoachPlanController(WorkoutPlanService workoutPlanService, DietPlanService dietPlanService, UserService userService, CoachService coachService) {
        this.workoutPlanService = workoutPlanService;
        this.dietPlanService = dietPlanService;
        this.userService = userService;
        this.coachService = coachService;
    }

    // Show all workout plans or filter by user
    @GetMapping("/workout-plans")
    public String allWorkoutPlans(@RequestParam(value = "userId", required = false) Long userId, Model model) {
        List<WorkoutPlan> plans = (userId != null) ? workoutPlanService.getPlansByUserId(userId) : workoutPlanService.getAllPlans();
        model.addAttribute("plans", plans);
        model.addAttribute("users", userService.getAllUsers());
        return "coach/workout-plans";
    }

    // Show form to create a workout plan
    @GetMapping("/workout-plans/new")
    public String newWorkoutPlanForm(Model model) {
        model.addAttribute("plan", new WorkoutPlan());
        model.addAttribute("users", userService.getAllUsers());
        return "coach/workout-plan-form";
    }

    // Handle workout plan creation
    @PostMapping("/workout-plans")
    public String createWorkoutPlan(@ModelAttribute WorkoutPlan plan, @RequestParam Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Coach coach = coachService.getCoachByUsername(auth.getName()).orElse(null);
        User user = userService.getUserById(userId).orElse(null);
        if (coach != null && user != null) {
            plan.setCreatedBy(coach);
            plan.setAssignedUser(user);
            workoutPlanService.savePlan(plan);
        }
        return "redirect:/coach/workout-plans";
    }

    // Show all diet plans or filter by user
    @GetMapping("/diet-plans")
    public String allDietPlans(@RequestParam(value = "userId", required = false) Long userId, Model model) {
        List<DietPlan> plans = (userId != null) ? dietPlanService.getPlansByUserId(userId) : dietPlanService.getAllPlans();
        model.addAttribute("plans", plans);
        model.addAttribute("users", userService.getAllUsers());
        return "coach/diet-plans";
    }

    // Show form to create a diet plan
    @GetMapping("/diet-plans/new")
    public String newDietPlanForm(Model model) {
        model.addAttribute("plan", new DietPlan());
        model.addAttribute("users", userService.getAllUsers());
        return "coach/diet-plan-form";
    }

    // Handle diet plan creation
    @PostMapping("/diet-plans")
    public String createDietPlan(@ModelAttribute DietPlan plan, @RequestParam Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Coach coach = coachService.getCoachByUsername(auth.getName()).orElse(null);
        User user = userService.getUserById(userId).orElse(null);
        if (coach != null && user != null) {
            plan.setCreatedBy(coach);
            plan.setAssignedUser(user);
            dietPlanService.savePlan(plan);
        }
        return "redirect:/coach/diet-plans";
    }

    // Show all clients (users)
    @GetMapping("/clients")
    public String showClients(Model model) {
        List<User> clients = userService.getAllUsers();
        model.addAttribute("clients", clients);
        return "coach/clients";
    }
} 