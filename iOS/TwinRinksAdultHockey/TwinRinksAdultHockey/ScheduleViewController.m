//
//  ScheduleViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/6/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "ScheduleViewController.h"
#import "ScheduleGamesViewController.h"
#import "MemoryManager.h"

@interface ScheduleViewController ()

@end

@implementation ScheduleViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self initButtons];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)refreshData:(id)sender
{
    MemoryManager *myManager = [[MemoryManager alloc]init];
    [myManager refreshData];
}

- (IBAction)settingsButtonPressed:(id)sender
{
    UIViewController *settingsViewController = [[self storyboard] instantiateViewControllerWithIdentifier:@"SettingsNavController"];
    settingsViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:settingsViewController animated:YES completion:nil];
}

- (IBAction)helpButtonPressed:(id)sender
{
    UIViewController *helpViewController = [[self storyboard] instantiateViewControllerWithIdentifier:@"HelpNavController"];
    helpViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:helpViewController animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *help = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"help.png"] style:UIBarButtonItemStylePlain target:self action:@selector(helpButtonPressed:)];
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(refreshData:)];
    
    NSArray *buttons = [[NSMutableArray alloc] initWithObjects:settings,help,nil];
    self.navigationItem.rightBarButtonItems = buttons;
    self.navigationItem.leftBarButtonItem = refresh;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    ScheduleGamesViewController *scheduleGamesViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"ScheduleGamesViewController"];
    
    switch (indexPath.row) {
        case 0:
            scheduleGamesViewController.dataToDisplay = @"AllTeams";
            break;
        case 1:
            scheduleGamesViewController.dataToDisplay = @"YourTeams";
            break;
        case 2:
            scheduleGamesViewController.dataToDisplay = @"AllGames";
            break;
        case 3:
            scheduleGamesViewController.dataToDisplay = @"Today";
            break;
        case 4:
            scheduleGamesViewController.dataToDisplay = @"Playoffs";
            break;
            
        default:
            scheduleGamesViewController.dataToDisplay = @"AllGames";
            break;
    }
    
    [self.navigationController pushViewController:scheduleGamesViewController animated:YES];
}

@end
